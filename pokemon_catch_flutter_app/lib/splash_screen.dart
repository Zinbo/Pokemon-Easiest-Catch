import 'package:flutter/material.dart';
import 'package:pokemoncatch/pokemon.dart';
import 'package:pokemoncatch/pokemon_list.dart';

class SplashScreen extends StatefulWidget {
  SplashScreen({Key key}) : super(key: key);

  @override
  _SplashScreenState createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  List<Game> games;
  List<Pokemon> pokemon;
  Future<void> loadData;

  @override
  void initState() {
    super.initState();
    loadData = Game.loadGames().then((games) {
      this.games = games;
      return Pokemon.loadPokemon();
    }).then((pokemon) {
      this.pokemon = pokemon;
      Navigator.pushReplacement(context,
          MaterialPageRoute(builder: (context) => PokemonList(pokemon: pokemon, games: games)));
    });
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<void>(
      future: this.loadData,
      builder: (context, snapshot) {
        return Container(
          color: Colors.red,
          child: Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: <Widget>[
                  Image.asset('assets/images/logo.png'),
                  new CircularProgressIndicator()
                ],
              )
          )
      );
      }
    );
  }
}