import 'pokemon.dart';

class FilterOptions {
  List<Game> allGames;
  Set<Game> selectedGames = new Set<Game>();
  bool hidePokemonThatCannotBeAcquired = false;
  bool hideOwnedPokemon = false;

  FilterOptions(List<Game> allGames) {
    this.allGames = allGames;
    selectedGames = allGames.toSet();
  }

  toggleHidePokemonThatCannotBeAcquired() {
    hidePokemonThatCannotBeAcquired = !hidePokemonThatCannotBeAcquired;
  }

  toggleHideOwnedPokemon(){
    hideOwnedPokemon = !hideOwnedPokemon;
  }

  reset() {
    selectedGames = allGames.toSet();
    hidePokemonThatCannotBeAcquired = false;
    hideOwnedPokemon = false;
  }
}