import 'package:http/http.dart' as http;
import 'dart:convert';

class Game {
  String name;
  static List<Game> loadedGames;

  Game({this.name});

  static Future<List<Game>> loadGames() async {
    var url = "https://pokeapi.co/api/v2/version/?limit=40";
    final response = await http.get(url);
    if (response.statusCode == 200) {
      var gamesJson = json.decode(response.body);
      List results = gamesJson["results"];
      Game.loadedGames = results.map((model) => Game.fromJson(model)).toList();
      return loadedGames;
    }
    else {
      throw("status code: ${response.statusCode} body: ${response.body}");
    }
  }

  static getGame(String name) {
    return loadedGames.firstWhere((game) => game.name == name);
  }

  factory Game.fromJson(Map<String, dynamic> resultJson) {
    return Game(name: resultJson["name"]);
  }
}

class Encounter {
  String location;
  Game game;
  int chance;

  Encounter({this.location, this.game, this.chance});

  static Future<List<Encounter>> getEncountersForPokemon(int pokemonId) async {
    var url = "https://pokeapi.co/api/v2/pokemon/$pokemonId/encounters";
    final response = await http.get(url);
    if (response.statusCode == 200) {
      Iterable l = json.decode(response.body);
      return l.map((model) => Encounter
          .getEncountersForLocation(model)).toList()
          .expand((a) => a).toList();
    }
  }

  static List<Encounter> getEncountersForLocation(Map<String, dynamic> encounterForLocationJson) {
    String location = encounterForLocationJson['location_area']['name'];
    Iterable versionDetails = encounterForLocationJson['version_details'];
    return versionDetails.map((version) => Encounter.fromJson(location, version)).toList();

  }

  factory Encounter.fromJson(String location, Map<String, dynamic> encounterDetailsForVersionJson) {
    Game game = Game.getGame(encounterDetailsForVersionJson['version']['name']);
    int maxChance = encounterDetailsForVersionJson['max_chance'];
    return Encounter(location: location, game: game, chance: maxChance);
  }
}

class Pokemon {
  int _id;
  String imageUrl;
  String _name;
  Set<Game> _games = new Set<Game>();
  List<Encounter> _encounters;
  bool _acquired = false;

  Pokemon(int id, String name, String imageUrl) {
    this._id = id;
    this._name = name;
    this.imageUrl = imageUrl;
  }

  static Future<List<Pokemon>> loadPokemon() async {
    // get base pokemon details
    var baseUrl = "https://pokeapi.co/api/v2/pokemon/";
    var pokemen = new List<Pokemon>();
    for(int i = 1; i <= 10; i++) {
      final response = await http.get(baseUrl + i.toString());
      if (response.statusCode == 200) {
        var pokemon = Pokemon.fromJson(json.decode(response.body));
        pokemen.add(pokemon);

        // get encounters
        var encounters = await Encounter.getEncountersForPokemon(pokemon._id);

        // get games from encounters
        encounters.forEach((element) => pokemon._games.add(element.game));
      }
      else {
        throw("status: ${response.statusCode} body: ${response.body}");
      }
    }

    return pokemen;
  }

  factory Pokemon.fromJson(Map<String, dynamic> json) {
    return Pokemon(json['id'], json['name'], json['sprites']['front_default']);
  }
}