package pe.com.lycsoftware.util;

import pe.com.lycsoftware.model.Category;

public class Constants
{
    public static Category categories[] = {
        new Category("Nombres"),
        new Category("Apellidos"),
        new Category("Cosas"),
        new Category("Animales"),
        new Category("Paises"),
        new Category("Ciudades"),
        new Category("Vegetales")
    };
    public static final String INIT_MESSAGE = "init_message";
    public static final int START_GAME = 0;
    public static final int STOP_GAME = 1;
    public static final int JOIN_GAME = 2;
}
