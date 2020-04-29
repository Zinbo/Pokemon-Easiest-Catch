import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

class Constants{
  static const String Subscribe = 'Subscribe';
  static const String Settings = 'Settings';
  static const String SignOut = 'Sign out';

  static const List<String> choices = <String>[
    Subscribe,
    Settings,
    SignOut
  ];
}

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Pokemon Catch',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: PokemonHomePage(title: 'Pokemon Catch Home Page'),
    );
  }
}

class Sprites {
  String frontDefault;

  Sprites({this.frontDefault});

  factory Sprites.fromJson(Map<String, dynamic> json) {
    return Sprites(
      frontDefault: json['front_default'],
    );
  }
}

class SpriteResponse {
  Sprites sprites;

  SpriteResponse({this.sprites});

  factory SpriteResponse.fromJson(Map<String, dynamic> json) {
    return SpriteResponse(
      sprites: Sprites.fromJson(json['sprites']),
    );
  }
}

class PokemonHomePage extends StatefulWidget {
  PokemonHomePage({Key key, this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  _PokemonHomeState createState() => _PokemonHomeState();
}

class _PokemonHomeState extends State<PokemonHomePage> {

  Future<List<SpriteResponse>> sprites;

  Future<SpriteResponse> getSprite(int number) async {
    final response = await http.get('https://pokeapi.co/api/v2/pokemon-form/' + number.toString());
    if(response.statusCode == 200) {
      var spriteResponse = SpriteResponse.fromJson(json.decode(response.body));
      return spriteResponse;
    }
  }

  Future<List<SpriteResponse>> getSprites() async {
    List<SpriteResponse> sprites = new List<SpriteResponse>();
    for(int i = 1; i < 50; i++) {
      var sprite = await getSprite(i);
      sprites.add(sprite);
    }
    return Future.value(sprites);
  }

  @override
  void initState() {
    super.initState();
    sprites = getSprites();
  }

  @override
  Widget build(BuildContext context) {
    final title = 'Grid List';

    return MaterialApp(
      title: title,
      home: Scaffold(
        appBar: AppBar(
          title: Text(title),
          actions: <Widget>[
            PopupMenuButton<String>(

              icon: Icon(Icons.filter_list),
              onSelected: choiceAction,
              itemBuilder: (BuildContext context){
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
                            children: <Widget>[
                              CheckboxListTile(
                                  title: Text("Red"),
                                  value: false),
                              CheckboxListTile(
                                  title: Text("Blue"),
                                  value: false),
                              CheckboxListTile(
                                  title: Text("Yellow"),
                                  value: false),
                              CheckboxListTile(
                                  title: Text("Gold"),
                                  value: false),
                              CheckboxListTile(
                                  title: Text("Silver"),
                                  value: false),
                              CheckboxListTile(
                                  title: Text("Crystal"),
                                  value: false),
                              CheckboxListTile(
                                  title: Text("Ruby"),
                                  value: false),
                              CheckboxListTile(
                                  title: Text("Saphire"),
                                  value: false),
                              CheckboxListTile(
                                  title: Text("Emerald"),
                                  value: false),
                            ]
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
//            IconButton(icon: Icon(Icons.filter_list), onPressed: ,)
          ],
        ),
        body: new Container(
          child: FutureBuilder<List<SpriteResponse>>(
            future: sprites,
            builder: (context, snapshot) {
              if (snapshot.hasError) {
              return Text("${snapshot.error}");
              }
              else if(snapshot.connectionState == ConnectionState.done) {
                var loadedSprites = snapshot.data;
                return GridView.count(
                    crossAxisCount: 8,
                    children: List.generate(loadedSprites.length, (index) {
                      return Center(
                        child: Transform.scale(
                        scale: 1.2,
                        child: new Container(
                            decoration: BoxDecoration(
                                image: DecorationImage(

                                    colorFilter: ColorFilter.matrix(<double>[
                                      0.2126,0.7152,0.0722,0,0,
                                      0.2126,0.7152,0.0722,0,0,
                                      0.2126,0.7152,0.0722,0,0,
                                      0,0,0,1,0,
                                    ]),
                                    image: NetworkImage(
                                        loadedSprites[index].sprites.frontDefault),
                                    fit: BoxFit.cover
                                )
                            )
                        ),
                      )
                      );
                    })
              );
              }
              return CircularProgressIndicator();
            },
          )
        ),
      ),
    );
  }

  void choiceAction(String choice){
    if(choice == Constants.Settings){
      print('Settings');
    }else if(choice == Constants.Subscribe){
      print('Subscribe');
    }else if(choice == Constants.SignOut){
      print('SignOut');
    }
  }
}
