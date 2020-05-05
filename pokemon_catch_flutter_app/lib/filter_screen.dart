import 'package:flutter/material.dart';

import 'pokemon.dart';
import 'pokemon_list.dart';

class FilterScreen extends StatefulWidget {
  List<Game> games;
  FilterOptions filterOptions;

  FilterScreen({Key key, this.games, this.filterOptions}) : super(key: key);

  @override
  _FilterScreenState createState() => _FilterScreenState();
}

class _FilterScreenState extends State<FilterScreen> {

  List<Widget> gameChips() {
    List<Widget> chips = new List<Widget>();
    chips.addAll(this.widget.games.map((game) {
      return ActionChip(
        label: Text(game.name),
        backgroundColor: this.widget.filterOptions.selectedGames.contains(game) ? Colors.red : Colors.grey,
        onPressed: () {
          if(this.widget.filterOptions.selectedGames.contains(game)) this.setState(() => this.widget.filterOptions.selectedGames.remove(game));
          else this.setState(() => this.widget.filterOptions.selectedGames.add(game));
        },
      );
    }).toList());

    return chips;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "Filter",
      home: Scaffold(
        appBar: AppBar(
          title: Text("Filter"),
          actions: <Widget>[
            IconButton(icon: Icon(Icons.close))
          ],
        ),
          body: Container(
              child: Column(
                children: <Widget>[
                  Container(height: 10),
                  Text("Can be caught in game:", style: TextStyle(fontSize: 20)),
                  Container(height: 10),
                  Wrap(
                    children: gameChips()
                  ),
                  Container(height: 30),
                   Row(
                    children: <Widget>[
                      Container(width: 10),
                      Text("Hide pokemon:", style: TextStyle(fontSize: 20)),
                      IconButton(icon: Icon(Icons.help)),
                      Slider(value: 1.0)
                    ],
                  ),
                  Container(height: 30),
                  Text("Ownership", style: TextStyle(fontSize: 20)),
                  Row(children: <Widget>[
                    Container(width: 10),
                    Text("Owned"),
                    Flexible(child: CheckboxListTile(value: true))
                  ]),
                  Row(children: <Widget>[
                    Container(width: 10),
                    Text("Not Owned"),
                    Flexible(child: CheckboxListTile(value: true))
                  ]),
                  RaisedButton(
                    child: Text("Reset"),
                  )
                ],
              ),
          ),
      )
    );
  }
}