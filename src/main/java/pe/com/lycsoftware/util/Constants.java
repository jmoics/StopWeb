package pe.com.lycsoftware.util;

import pe.com.lycsoftware.model.Category;

public class Constants
{
    public static final String GAMEBOARD_KEY = "gameBoard";
    public static final String CATEGORY_KEY = "category";
    public static final String GAMEROOM_KEY = "gameRoom";
    public static final String GAMEUSER_KEY = "gameUser";

    public static Category categories[] = {
        new Category("Nombres"),
        new Category("Apellidos"),
        new Category("Cosas"),
        new Category("Animales"),
        new Category("Paises/Ciudades"),
        new Category("Frutas/Verduras"),
        new Category("Colores"),
        new Category("Marcas"),
        new Category("Series/Peliculas"),
        new Category("Canciones")
    };
    public static final String INIT_MESSAGE = "init_message";
    public static final int START_GAME = 0;
    public static final int STOP_GAME = 1;
    public static final int JOIN_GAME = 2;
    public static final int READY_GAME = 3;
    public static final int RESULT_GAME = 4;
    public static final int LOGOUT_GAME = 5;
    
    public static final Integer CELL_WIN = 100;
    public static final Integer CELL_DRAW = 50;
    public static final Integer CELL_LOSE = 0;
}
