import 'package:http/http.dart' as http;
import 'dart:convert';

class Game {
  int _id;
  String name;

  static Future<List<Game>> loadGames() async {
    var url = "https://pokeapi.co/api/v2/version/?limit=40";
  }
}

class Encounter {
  String location;
  Game game;
  int chance;

  static Future<List<Encounter>> getEncounters(int pokemonId) async {
    var url = "https://pokeapi.co/api/v2/pokemon/$pokemonId/encounters";
    final response = await http.get(url);
    if (response.statusCode == 200) {
      Iterable l = json.decode(response.body);
      var encounters = l.map((model) => Encounter.fromJson(json.decode(model))).toList();
    }
  }

  factory Encounter.fromJson(Map<String, dynamic> json) {
    String location = json['location_area']['name'];
    Iterable l = json['version_details'];
    for(var versionDetails in l) {
      int maxChance = versionDetails.max_chance;
      String game = versionDetails.version.name;
    }

    return Encounter(json['location_area']['name'], json['version_details'][''], json['sprites']['front_default']);
  }
}

class Pokemon {
  int _id;
  String _imageUrl;
  String _name;
  Set<Game> _games = new Set<Game>();
  List<Encounter> _encounters;
  bool _acquired = false;

  Pokemon(int id, String name, String imageUrl) {
    this._id = id;
    this._name = name;
    this._imageUrl = imageUrl;
  }

  static Future<List<Pokemon>> loadPokemon() async {
    // get base pokemon details
    var baseUrl = "https://pokeapi.co/api/v2/pokemon";
    var pokemen = new List<Pokemon>();
    for(int i = 1; i <= 10; i++) {
      final response = await http.get(baseUrl + i.toString());
      if (response.statusCode == 200) {
        var pokemon = Pokemon.fromJson(json.decode(response.body));
        pokemen.add(pokemon);

        // get encounters
        var encounters = await Encounter.getEncounters(pokemon._id);

        // get games from encounters
        encounters.forEach((element) => pokemon._games.add(element.game));

      }
    }


  }

  factory Pokemon.fromJson(Map<String, dynamic> json) {
    return Pokemon(json['id'], json['name'], json['sprites']['front_default']);
  }
}