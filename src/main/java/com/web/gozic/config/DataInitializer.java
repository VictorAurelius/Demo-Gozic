package com.web.gozic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.web.gozic.entity.Clothes;
import com.web.gozic.repository.ClothesRepository;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ClothesRepository clothesRepository;

    private Random random = new Random();

    private List<String> menNames = Arrays.asList(
            "Áo sơ mi nam", "Quần jean nam", "Áo thun nam", "Áo khoác nam",
            "Quần tây nam", "Áo polo nam", "Áo vest nam", "Quần short nam");

    private List<String> womenNames = Arrays.asList(
            "Áo sơ mi nữ", "Váy công sở", "Áo thun nữ", "Áo khoác nữ",
            "Quần jean nữ", "Đầm dự tiệc", "Chân váy", "Áo kiểu nữ");

    private List<String> boysNames = Arrays.asList(
            "Áo thun bé trai", "Quần jean bé trai", "Áo khoác bé trai",
            "Quần short bé trai", "Bộ quần áo bé trai", "Áo polo bé trai");

    private List<String> girlsNames = Arrays.asList(
            "Váy bé gái", "Áo thun bé gái", "Quần jean bé gái",
            "Đầm bé gái", "Bộ quần áo bé gái", "Áo khoác bé gái");

    @Override
    public void run(String... args) throws Exception {
        File imageDir = new File("src/main/resources/static/image");
        if (!imageDir.exists())
            return;

        processDirectory(imageDir);
    }

    private void processDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files == null)
            return;

        for (File file : files) {
            if (file.isDirectory()) {
                if (!file.getName().equals("logo")) { // Skip logo directory
                    processDirectory(file);
                }
            } else if (isImageFile(file.getName())) {
                createClothes(file);
            }
        }
    }

    private boolean isImageFile(String name) {
        String lowercaseName = name.toLowerCase();
        return lowercaseName.endsWith(".jpg") || lowercaseName.endsWith(".jpeg") ||
                lowercaseName.endsWith(".png") || lowercaseName.endsWith(".gif");
    }

    private void createClothes(File imageFile) {
        String relativePath = imageFile.getPath()
                .replace("src\\main\\resources\\static\\", "")
                .replace("\\", "/");

        String parentDir = imageFile.getParentFile().getName().toLowerCase();
        String type = determineType(parentDir);
        String name = generateName(type);
        double price = 100000 + random.nextInt(900000); // 100,000 to 1,000,000 VND

        Clothes clothes = new Clothes();
        clothes.setName(name);
        clothes.setPrice(price);
        clothes.setType(type);
        clothes.setImage(relativePath);

        clothesRepository.save(clothes);
    }

    private String determineType(String directory) {
        switch (directory) {
            case "donam":
                return "men";
            case "donu":
                return "women";
            case "dotreem":
                return "children";
            default:
                return "other";
        }
    }

    private String generateName(String type) {
        List<String> names;
        switch (type) {
            case "men":
                names = menNames;
                break;
            case "women":
                names = womenNames;
                break;
            case "boys":
                names = boysNames;
                break;
            case "girls":
                names = girlsNames;
                break;
            default:
                names = menNames;
        }
        return names.get(random.nextInt(names.size()));
    }
}