import 'package:flutter/material.dart';
import 'package:pokemoncatch/database_helpers.dart';
import 'package:pokemoncatch/pokemon_list.dart';

import 'pokemon.dart';

class SelectGames extends StatefulWidget {
  final List<Game> games;
  final List<Pokemon> pokemon;

  SelectGames({Key key, this.games, this.pokemon}) : super(key: key);

  @override
  _SelectGamesState createState() => _SelectGamesState();
}

class _SelectGamesState extends State<SelectGames> {

  DatabaseHelper helper = DatabaseHelper.instance;
  List<Game> selectedGames = new List<Game>();
  Future<void> savingGameData;

  @override
  void initState() {
    super.initState();
    helper.getAllSelectedGames()
      .then((savedGames) {
        if(savedGames.isNotEmpty) Navigator.pushReplacement(context,
        MaterialPageRoute(builder: (context) => PokemonList(pokemon: this.widget.pokemon, games: this.widget.games)));
    });
  }

  List<Widget> gameChips() {
    List<Widget> chips = new List<Widget>();
   chips.addAll(this.widget.games.map((game) {
      return ActionChip(
        label: Text(game.name),
        backgroundColor: this.selectedGames.contains(game) ? Colors.red : Colors.grey,
        onPressed: () {
          if(this.selectedGames.contains(game)) this.setState(() => this.selectedGames.remove(game));
          else this.setState(() => this.selectedGames.add(game));
        },
      );
    }).toList());

   return chips;
  }

  Widget showLoaderIfSavingData() {
    if(savingGameData == null) return Container();
    else {
      return FutureBuilder(
        future: savingGameData,
        builder: (context, snapshot) {
          if(snapshot.connectionState != ConnectionState.done) {
            return CircularProgressIndicator();
          }
          return Container();
        },
      );
    }
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
                showLoaderIfSavingData()
            ]
            )
          )
        ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          saveDataAndNavigateToNextPage();
        },
        child: Icon(Icons.check),
        backgroundColor: Colors.red,
      ),
    );
  }

  void saveDataAndNavigateToNextPage() async {
    setState(() {
      savingGameData = helper.insertAll(selectedGames);
    });
    await savingGameData;
    Navigator.pushReplacement(context,
        MaterialPageRoute(builder: (context) => PokemonList(pokemon: this.widget.pokemon, games: this.widget.games)));
  }
}