package com.stacktobasics.pokemoncatchbackend;

import com.stacktobasics.pokemoncatchbackend.domain.Game;
import com.stacktobasics.pokemoncatchbackend.domain.GameRepository;
import com.stacktobasics.pokemoncatchbackend.domain.Pokemon;
import com.stacktobasics.pokemoncatchbackend.domain.PokemonRepository;
import com.stacktobasics.pokemoncatchbackend.domain.evolution.EvolutionChain;
import com.stacktobasics.pokemoncatchbackend.domain.evolution.EvolutionChainRepository;
import com.stacktobasics.pokemoncatchbackend.infra.PokeApiClient;
import com.stacktobasics.pokemoncatchbackend.infra.dtos.EvolutionNode;
import com.stacktobasics.pokemoncatchbackend.infra.dtos.GamesDTO;
import com.stacktobasics.pokemoncatchbackend.infra.dtos.evolution.*;
import lombok.NonNull;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.stacktobasics.pokemoncatchbackend.domain.Game.UNUSED_GAMES;

@Component
public class PopulateDbWithPokeData {

    public static final int FEMALE = 1;
    public static final int MALE = 2;
    public static final int GREATER_ATTACK = 1;
    private static final int SAME_ATTACK_AND_DEFENSE = 0;
    private static final int GREATER_DEFENSE = -1;
    private final PokeApiClient client;
    private final GameRepository gameRepository;
    private final PokemonRepository pokemonRepository;
    private EvolutionChainRepository evolutionChainRepository;
    private final Pattern pokedexNumberFromUrl = Pattern.compile("[^v](\\d+)");

    public PopulateDbWithPokeData(PokeApiClient client, GameRepository gameRepository,
                                  PokemonRepository pokemonRepository, EvolutionChainRepository evolutionChainRepository) {
        this.client = client;
        this.gameRepository = gameRepository;
        this.pokemonRepository = pokemonRepository;
        this.evolutionChainRepository = evolutionChainRepository;
    }


    public void populateGames() {
        List<Game> savedGames = new ArrayList<>(gameRepository.findAll());
        GamesDTO games = client.getGames();
        games.results.stream().filter(newGame -> {
            if (UNUSED_GAMES.contains(newGame.name)) return false;
            return savedGames.stream().noneMatch(savedGame -> newGame.name.equals(savedGame.getName()));
        }).forEach(game -> gameRepository.save(new Game(game.name)));
    }

    private void downloadAndSaveImage(int id, String descriptionImage, String folder) {
        try (InputStream in = new URL(descriptionImage).openStream()) {
            Files.copy(in, Paths.get(String.format("images/%s/%d.png", folder, id)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populatePokemon() {
        List<Pokemon> pokemon = getPokemon();
        Map<Integer, Pokemon> pokemonById = pokemon.stream().collect(Collectors.toMap(Pokemon::getPokedexNumber, p -> p));
        List<EvolutionChain> evolutionChains = getEvolutionChains();
        evolutionChains.forEach(e -> e.getAllPokemonInChain().forEach(
                id -> pokemonById.computeIfPresent(id,
                        (integer, found) -> {
                            found.setEvolutionChainId(e.getId());
                            return found;
                        })
        ));
        pokemonRepository.saveAll(pokemon);
        evolutionChainRepository.saveAll(evolutionChains);
    }

    private List<Pokemon> getPokemon() {
        Map<Integer, Integer> generations = new HashMap<>();
        client.getGenerations().forEach(generationDTO -> {
            int generationNo = generationDTO.id;
            generationDTO.species.stream()
                    .map(this::getPokedexNumberFromUrl)
                    .forEach(pokemonNo -> {
                        if (generations.containsKey(pokemonNo))
                            throw new InternalException("Generations map already contained pokemon no: " + pokemonNo);
                        generations.put(pokemonNo, generationNo);
                    });
        });

        List<Pokemon> pokemon = client.getPokemon().stream()
                .map(dto -> {
                    String listImage = dto.sprites.frontDefault;
                    String descriptionImage = dto.sprites.other.officialArtwork.frontDefault;
                    downloadAndSaveImage(dto.id, listImage, "list");
                    downloadAndSaveImage(dto.id, descriptionImage, "description");
                    return new Pokemon(dto.id, dto.name, generations.get(dto.id));
                })
                .collect(Collectors.toList());

        pokemon.forEach(p ->
            client.getEncountersForPokemon(p.getPokedexNumber())
                .forEach(dto ->
                        dto.versionDetails.forEach(v ->
                                v.encounterDetails.forEach(ed -> {
                                    if(CollectionUtils.isEmpty(ed.conditionalValues)) {
                                        p.addEncounter(ed.chance, dto.locationArea.name,
                                                v.version.name, ed.method.name, "none");
                                    }
                                    else ed.conditionalValues.forEach(cv ->
                                            p.addEncounter(ed.chance, dto.locationArea.name,
                                            v.version.name, ed.method.name, cv.name));

                                }))));
        return pokemon;
    }

    private List<EvolutionChain> getEvolutionChains() {
        List<PokemonEvolutionDTO> evolutionChains = client.getEvolutionChains();

        return evolutionChains.stream().map(dto -> {
            EvolutionChain ec = new EvolutionChain(dto.id);

            // set baby if exists
            ChainDTO chainDTO = dto.chain;
            NamedResourceDTO speciesNavigationDTO = chainDTO.species;
            int pokedexNumber = getPokedexNumberFromUrl(speciesNavigationDTO);
            if (chainDTO.isBaby) {
                ec.setBaby(pokedexNumber, Optional.ofNullable(dto.babyTriggerItemDTO)
                        .map(babyTriggerItemDTO -> babyTriggerItemDTO.name)
                        .orElse(null));
            }

            if (!CollectionUtils.isEmpty(chainDTO.evolutionDetails)) {
                // It looks like all evolution details on the top level chain are empty
                // throw an exception if not so I can change the logic
                throw new InternalException("evolution details were not empty for pokemon " + pokedexNumber);
            }

            List<EvolvesToDTO> evolvesTo = chainDTO.evolvesTo;
            Stack<EvolutionNode> stack = new Stack<>();
            evolvesTo.stream().map(e -> new EvolutionNode(e, pokedexNumber)).forEach(stack::add);
            while (!stack.isEmpty()) {
                EvolutionNode pop = stack.pop();
                EvolvesToDTO evolvesToDTO = pop.getEvolvesToDTO();
                int to = getPokedexNumberFromUrl(evolvesToDTO.species);
                evolvesToDTO.evolutionDetails.forEach(ed -> {
                    List<Pair<String, String>> criteria = getCriteria(ed);
                    String trigger = ed.trigger.name;
                    ec.addEvolution(pop.getFrom(), to, criteria, trigger);
                });
                evolvesToDTO.evolvesTo.stream().map(e -> new EvolutionNode(e, to)).forEach(stack::add);
            }
            return ec;
        }).collect(Collectors.toList());
    }

    private List<Pair<String, String>> getCriteria(@NonNull EvolutionDetailsDTO ed) {
        List<Pair<String, String>> criteria = new ArrayList<>();
        Integer gender = ed.gender;
        if(gender != null) {
            if(gender == FEMALE) criteria.add(Pair.of("gender", "female"));
            else if(gender == MALE) criteria.add(Pair.of("gender", "male"));
            else throw new InternalException("gender value not expected, got: " + gender);
        }
        if(ed.heldItem != null) criteria.add(Pair.of("held item", ed.heldItem.name));
        if(ed.item != null) criteria.add(Pair.of("item to use", ed.item.name));
        if(ed.knownMove != null) criteria.add(Pair.of("known move", ed.knownMove.name));
        if(ed.knownMoveType != null) criteria.add(Pair.of("known move type", ed.knownMoveType.name));
        if(ed.location != null) criteria.add(Pair.of("location", ed.location.name));
        if(ed.minAffection != null) criteria.add(Pair.of("min affection", String.valueOf(ed.minAffection)));
        if(ed.minBeauty != null) criteria.add(Pair.of("min beauty", String.valueOf(ed.minBeauty)));
        if(ed.minHappiness != null) criteria.add(Pair.of("min happiness", String.valueOf(ed.minHappiness)));
        if(ed.minLevel != null) criteria.add(Pair.of("min level", String.valueOf(ed.minLevel)));
        if(BooleanUtils.isTrue(ed.needsOverworldRain)) criteria.add(Pair.of("needs overworld rain", "true"));
        if(ed.partySpecies != null) criteria.add(Pair.of("party species", ed.partySpecies.name));
        if(ed.partyType != null) criteria.add(Pair.of("party type", ed.partyType.name));
        Integer relativePhysicalStats = ed.relativePhysicalStats;
        if(relativePhysicalStats != null) {
           if(relativePhysicalStats == GREATER_ATTACK) criteria.add(Pair.of("relative physical stats", "greater attack"));
           if(relativePhysicalStats == SAME_ATTACK_AND_DEFENSE) criteria.add(Pair.of("relative physical stats", "same attack and defense"));
            if(relativePhysicalStats == GREATER_DEFENSE) criteria.add(Pair.of("relative physical stats", "greater defense"));
        }
        if(!StringUtils.isEmpty(ed.timeOfDay)) criteria.add(Pair.of("time of day", ed.timeOfDay));
        if(ed.tradeSpecies != null) criteria.add(Pair.of("trade species", ed.tradeSpecies.name));
        if(BooleanUtils.isTrue(ed.turnUpsideDown)) criteria.add(Pair.of("turn 3DS upside down", "true"));
        return criteria;
    }

    private int getPokedexNumberFromUrl(NamedResourceDTO dto) {
        if(dto == null) throw new InternalException("Species cannot be null");
        String url = dto.url;
        if(url == null) throw new InternalException("Species URL cannot be null");
        Matcher matcher = pokedexNumberFromUrl.matcher(url);
        // return last number
        if(!matcher.find()) throw new InternalException("Could not find matching pattern for number in url: " + url);
        String pokedexNumberAsString = matcher.group(1);
        try {
            return Integer.parseInt(pokedexNumberAsString);
        } catch(NumberFormatException e) {
            throw new InternalException("Could not parse " + pokedexNumberAsString + " as int");
        }
    }

}
