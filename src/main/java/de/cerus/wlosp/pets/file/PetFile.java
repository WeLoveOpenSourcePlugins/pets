package de.cerus.wlosp.pets.file;

import com.google.gson.*;
import de.cerus.wlosp.pets.model.CustomPetModel;
import de.cerus.wlosp.pets.pet.impl.CustomPetImpl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class PetFile {

    private final File file;

    public PetFile(File file) {
        this.file = file;
    }

    public CustomPetImpl loadPetFromFile(JavaPlugin plugin) throws FileNotFoundException, JsonIOException,
            JsonSyntaxException, NullPointerException {
        FileInputStream fileInputStream = new FileInputStream(file);
        JsonElement parsedElement = new JsonParser().parse(new InputStreamReader(fileInputStream));

        CustomPetModel model = new Gson().fromJson(parsedElement, CustomPetModel.class);
        Class<? extends LivingEntity> entityClass = parseFromEntityType(model.getEntityType());

        return new CustomPetImpl(plugin, model.getName(), entityClass, model.getFlags());
    }

    private Class<? extends LivingEntity> parseFromEntityType(EntityType type) {
        Class<?> aClass;
        try {
            aClass = Class.forName("org.bukkit.entity." + toCamelCase(type.name()));
        } catch (ClassNotFoundException e) {
            return null;
        }
        return aClass.isAssignableFrom(LivingEntity.class) ? (Class<? extends LivingEntity>) aClass : null;
    }

    private String toCamelCase(String str) {
        String[] arr = str.split(" ");
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i];
            arr[i] = Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase();
        }
        return String.join("", arr);
    }

}
