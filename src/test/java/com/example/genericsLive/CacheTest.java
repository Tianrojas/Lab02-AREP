package com.example.genericsLive;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class CacheTest {

    @Test
    public void testPutAndGet() {
        // Prueba para verificar que se pueda almacenar y recuperar un objeto JSON correctamente

        // Creamos un objeto JSON para almacenar en caché
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", "value");

        // Almacenamos el objeto JSON en caché
        Cache.put("testKey", jsonObject);

        // Recuperamos el objeto JSON de la caché
        JSONObject cachedObject = Cache.get("testKey");

        // Verificamos que el objeto recuperado sea el mismo que el objeto original
        assertEquals(jsonObject.toString(), cachedObject.toString());
    }

    @Test
    public void testContainsKey() {
        // Prueba para verificar que se pueda determinar si una clave está presente en la caché

        // Añadimos una clave a la caché
        Cache.put("testKey", new JSONObject());

        // Verificamos que la clave esté presente en la caché
        assertTrue(Cache.containsKey("testKey"));

        // Verificamos que una clave que no está presente devuelva false
        assertFalse(Cache.containsKey("nonExistentKey"));
    }
}

