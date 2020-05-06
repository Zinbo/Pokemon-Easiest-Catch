import 'package:flutter/material.dart';
import 'package:pokemoncatch/filter_screen.dart';
import 'database_helpers.dart';
import 'filter_options.dart';
import 'pokemon.dart';




class PokemonList extends StatefulWidget {
  final List<Pokemon> pokemon;
  final List<Game> games;

  PokemonList({Key key, this.pokemon, this.games}) : super(key: key);

  @override
  _PokemonListState createState() => _PokemonListState();
}

class _PokemonListState extends State<PokemonList> {
  List<Pokemon> pokemon;
  List<Game> allGames;
  Future<List<Game>> savedGames;
  DatabaseHelper helper = DatabaseHelper.instance;
  FilterOptions filterOptions;

  @override
  void initState() {
    super.initState();
    pokemon = this.widget.pokemon;
    allGames = this.widget.games;
    savedGames = helper.getAllSelectedGames();
    filterOptions = new FilterOptions(allGames);
  }

  @override
  Widget build(BuildContext context) {
    final title = 'Pokemon List';

    return MaterialApp(
      title: title,
      home: Scaffold(
        appBar: AppBar(
          title: Text(title),
          actions: <Widget>[
            IconButton(icon: Icon(Icons.filter_list), onPressed: () => {
              Navigator.push(context,
                  MaterialPageRoute(builder:
                      (context) => FilterScreen(games: allGames, filterOptions: filterOptions)))
            })
          ],
        ),
        body: new Container(
          child: GridView.count(
              crossAxisCount: 8,
              children: List.generate(pokemon.length, (index) {
                bool shouldShowPokemon = false;
                for(Game game in filterOptions.selectedGames) {
                  if(pokemon[index].games.contains(game)) {
                    shouldShowPokemon = true;
                    break;
                  }
                }

                if(!shouldShowPokemon) return Container();
                return Center(
                    child: Transform.scale(
                      scale: 1.2,
                      child: new Container(
                          decoration: BoxDecoration(
                              image: DecorationImage(

                                  colorFilter: ColorFilter.matrix(
                                      <double>[
                                        0.2126, 0.7152, 0.0722, 0, 0,
                                        0.2126, 0.7152, 0.0722, 0, 0,
                                        0.2126, 0.7152, 0.0722, 0, 0,
                                        0, 0, 0, 1, 0,
                                      ]),
                                  image: NetworkImage(pokemon[index].imageUrl),
                                  fit: BoxFit.cover
                              )
                          )
                      ),
                    )
                );
              })
          )
        )
      ),
    );
  }
}