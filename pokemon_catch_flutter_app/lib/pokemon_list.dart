import 'package:flutter/material.dart';
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
  List<Game> games;
  List<Game> selectedGames = [];

  @override
  void initState() {
    super.initState();
    pokemon = this.widget.pokemon;
    games = this.widget.games;
  }

  void printChoice(String choice) {
    print(choice);
  }

  Widget filerPopupMenu() {
    return new Container(
      child: PopupMenuButton<String>(

        icon: Icon(Icons.filter_list),
        onSelected: printChoice,
        itemBuilder: (BuildContext context) {
          var list = List<PopupMenuEntry<String>>();
          list.add(
              PopupMenuItem(
                  child: Text("Filter by:")
              )
          );

          list.add(
              PopupMenuItem(
                  child: ExpansionTile(
                      title: Text("Game"),
                      children: this.games.map((game) {
                        return CheckboxListTile(
                            key: Key(game.name),
                            title: Text(game.name),
                            value: selectedGames.contains(game),
                            onChanged: (bool selected) {
                              _filterPokemonByGame(selected, game);
                            }
                            );
                      }).toList()
                  )
              )
          );

          list.add(
              PopupMenuItem(

                  child: ExpansionTile(
                      title: Text("Owned"),
                      children: <Widget>[
                        RadioListTile(
                            title: Text("Owned"),
                            value: false),
                        RadioListTile(
                            title: Text("Not Owned"),
                            value: false)
                      ]
                  )
              )
          );

          list.add(
              PopupMenuItem(

                  child: ExpansionTile(
                      title: Text("Breeding"),
                      children: <Widget>[
                        CheckboxListTile(
                            title: Text("Can be bred"),
                            value: false),
                        CheckboxListTile(
                            title: Text("Cannot be bred"),
                            value: false)
                      ]
                  )
              )
          );

          return list;
        },
      )
    );
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
            filerPopupMenu()
//            IconButton(icon: Icon(Icons.filter_list), onPressed: ,)
          ],
        ),
        body: new Container(
          child: GridView.count(
              crossAxisCount: 8,
              children: List.generate(pokemon.length, (index) {
                if(selectedGames.isNotEmpty) {
                  bool shouldShowPokemon = false;
                  for(Game game in selectedGames) {
                    if(pokemon[index].games.contains(game)) {
                      shouldShowPokemon = true;
                      break;
                    }
                  }

                  if(!shouldShowPokemon) return Container();
                }
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

  void _filterPokemonByGame(bool selected, Game game) {
    setState(() {
      if(selected) selectedGames.add(game);
      else selectedGames.remove(game);
    });
  }
}