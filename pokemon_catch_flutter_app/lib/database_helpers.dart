import 'dart:io';
import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';
import 'package:path_provider/path_provider.dart';
import 'pokemon.dart';

final String tableWords = 'selected_games';
final String columnId = '_id';
final String columnName = 'name';


class SelectedGame extends Game {
  int id;

  SelectedGame(this.id, String name) : super(name: name);

  SelectedGame.fromMap(Map<String, dynamic> map) {
    id = map[columnId];
    name = map[columnName];
  }

  // convenience method to create a Map from this Word object
  Map<String, dynamic> toMap() {
    var map = <String, dynamic>{
      columnName: name
    };
    if (id != null) {
      map[columnId] = id;
    }
    return map;
  }
}

// singleton class to manage the database
class DatabaseHelper {

  // This is the actual database filename that is saved in the docs directory.
  static final _databaseName = "MyDatabase.db";
  // Increment this version when you need to change the schema.
  static final _databaseVersion = 1;

  // Make this a singleton class.
  DatabaseHelper._privateConstructor();
  static final DatabaseHelper instance = DatabaseHelper._privateConstructor();

  // Only allow a single open connection to the database.
  static Database _database;
  Future<Database> get database async {
    if (_database != null) return _database;
    _database = await _initDatabase();
    return _database;
  }

  // open the database
  _initDatabase() async {
    // The path_provider plugin gets the right directory for Android or iOS.
    Directory documentsDirectory = await getApplicationDocumentsDirectory();
    String path = join(documentsDirectory.path, _databaseName);
    // Open the database. Can also add an onUpdate callback parameter.
    return await openDatabase(path,
        version: _databaseVersion,
        onCreate: _onCreate);
  }

  // SQL string to create the database
  Future _onCreate(Database db, int version) async {
    await db.execute('''
              CREATE TABLE $tableWords (
                $columnId INTEGER PRIMARY KEY AUTOINCREMENT,
                $columnName TEXT NOT NULL
              )
              ''');
  }

  // Database helper methods:

  Future<int> insert(Game game) async {
    Database db = await database;
    SelectedGame selectedGame = SelectedGame(null, game.name);
    int id = await db.insert(tableWords, selectedGame.toMap());
    return id;
  }

  Future<void> insertAll(List<Game> games) async {
    Database db = await database;
    for(Game game in games) {
      SelectedGame selectedGame = SelectedGame(null, game.name);
      await db.insert(tableWords, selectedGame.toMap());
    }
  }

  Future<SelectedGame> queryWord(int id) async {
    Database db = await database;
    List<Map> maps = await db.query(tableWords,
        columns: [columnId, columnName],
        where: '$columnId = ?',
        whereArgs: [id]);
    if (maps.length > 0) {
      return SelectedGame.fromMap(maps.first);
    }
    return null;
  }
}