import 'package:flutter/material.dart';
import 'filter_options.dart';
import 'pokemon.dart';

class FilterScreen extends StatefulWidget {
  final List<Game> games;
  final FilterOptions filterOptions;

  FilterScreen({Key key, this.games, this.filterOptions}) : super(key: key);

  @override
  _FilterScreenState createState() => _FilterScreenState();
}

class _FilterScreenState extends State<FilterScreen> {
  List<Widget> gameChips() {
    Set<Game> selectedGames = widget.filterOptions.selectedGames;
    List<Widget> chips = new List<Widget>();
    chips.addAll(widget.games.map((game) {
      return ActionChip(
        label: Text(game.name),
        backgroundColor:
            selectedGames.contains(game) ? Colors.red : Colors.grey,
        onPressed: () {
          if (selectedGames.contains(game))
            this.setState(() => selectedGames.remove(game));
          else
            this.setState(() => selectedGames.add(game));
        },
      );
    }).toList());

    return chips;
  }

  //TODO refactor this into own components
  @override
  Widget build(BuildContext context) {
    FilterOptions filterOptions = widget.filterOptions;
    return MaterialApp(
        title: "Filter",
        home: Scaffold(
          appBar: AppBar(
            backgroundColor: Colors.red,
            title: Text("Filter"),
            actions: <Widget>[
              IconButton(
                  icon: Icon(Icons.close),
                  onPressed: () => Navigator.pop(context))
            ],
          ),
          body: Container(
            child: Column(
              children: <Widget>[
                Container(height: 10),
                Text("Can be caught in game:", style: TextStyle(fontSize: 20)),
                Container(height: 10),
                Wrap(children: gameChips()),
                Container(height: 30),
                buildSwitchRow(context,
                  title: "Hide pokemon that can't be aquired:",
                  tooltip: "By default pokemon that can't be acquired from selected games are greyed out, turning this on will hide them completely",
                  value: filterOptions.hidePokemonThatCannotBeAcquired,
                  onChangedFunc: (newValue) => setState(
                        () => filterOptions.hidePokemonThatCannotBeAcquired = newValue)
                ),
                buildSwitchRow(context,
                    title: "Hide owned pokemon:",
                    tooltip: "By default pokemon that you own will be shown, turning this on will hide them",
                    value: filterOptions.hideOwnedPokemon,
                    onChangedFunc: (newValue) => setState(
                            () => filterOptions.hideOwnedPokemon = newValue)
                ),
                RaisedButton(
                  child: Text("Reset"),
                  onPressed: () => setState(() => filterOptions.reset()),
                )
              ],
            ),
          ),
          floatingActionButton: FloatingActionButton(
            onPressed: () {
              Navigator.pop(context);
            },
            child: Icon(Icons.check),
            backgroundColor: Colors.red,
          ),
        ));
  }

  Row buildSwitchRow(
      BuildContext context, {String title, String tooltip, bool value, Function(bool) onChangedFunc}) {
    return Row(
      children: <Widget>[
        Container(width: 10),
        Container(
            width: MediaQuery.of(context).size.width * 0.5,
            child: Text(title,
                style: TextStyle(fontSize: 15))),
        IconButton(
          icon: Icon(Icons.help),
          tooltip: tooltip),
        Switch(
            value: value,
            onChanged: (newValue) => onChangedFunc(newValue))
      ],
    );
  }
}
