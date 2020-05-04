import 'package:flutter/material.dart';
import 'package:pokemoncatch/database_helpers.dart';
import 'package:pokemoncatch/pokemon_list.dart';

import 'pokemon.dart';

class SelectGames extends StatefulWidget {
  List<Game> games;
  List<Pokemon> pokemon;
  List<Game> selectedGames = new List<Game>();

  SelectGames({Key key, this.games, this.pokemon}) : super(key: key);

  @override
  _SelectGamesState createState() => _SelectGamesState();
}

class _SelectGamesState extends State<SelectGames> {

  DatabaseHelper helper = DatabaseHelper.instance;

  List<Widget> gameChips() {
    List<Widget> chips = new List<Widget>();
   chips.addAll(this.widget.games.map((game) {
      return ActionChip(
        label: Text(game.name),
        backgroundColor: this.widget.selectedGames.contains(game) ? Colors.red : Colors.grey,
        onPressed: () {
          if(this.widget.selectedGames.contains(game)) this.setState(() => this.widget.selectedGames.remove(game));
          else this.setState(() => this.widget.selectedGames.add(game));
        },
      );
    }).toList());

   return chips;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Container(
          child: Center(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: <Widget>[
                Text('Select Owned Games',  style: TextStyle(fontSize: 30)),
                Container(height: 10),
                Wrap(
                  children: gameChips(),
                ),
            ]
            )
          )
        ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          // TODO: need to save
          // TODO: then show loader whilst waiting
          // TODO: then navigate to new route
          Navigator.pushReplacement(context,
                  MaterialPageRoute(builder: (context) => PokemonList(pokemon: this.widget.pokemon, games: this.widget.games)));
        },
        child: Icon(Icons.check),
        backgroundColor: Colors.red,
      ),
    );
  }
}