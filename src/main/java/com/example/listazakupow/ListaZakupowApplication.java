package com.example.listazakupow;

import com.example.listazakupow.Service.IkeaProductScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ListaZakupowApplication {

    public static void main(String[] args)  {
        WebDriver driver = new ChromeDriver();
        IkeaProductScraper ikeaProductScraper = new IkeaProductScraper();

        try {
            Map<String, String> productsCategoryLinks = ikeaProductScraper.getLink("div.hnf-carousel-slide");
            Map<String, Map<String, String>> outerMap = new HashMap<>();

            for (Map.Entry<String, String> map : productsCategoryLinks.entrySet()) {
                Map<String, String> itemCategoryLink = ikeaProductScraper.getItemCategoryLink(map.getValue());
                outerMap.put(map.getKey(), itemCategoryLink);
            }

            for (Map.Entry<String, Map<String, String>> outerEntry : outerMap.entrySet()) {
                Map<String, String> innerMap = outerEntry.getValue();

                for (Map.Entry<String, String> innerEntry : innerMap.entrySet()) {
                    driver.get(innerEntry.getValue());
                    String html = driver.getPageSource();
                    Document doc = Jsoup.parse(html);

                    Elements links = doc.select("a.plp-pagination-button__page-button");

                    int pageNumber = 1;
                    for (Element link : links) {
                        String pageNumberText = link.text();
                        pageNumber = Integer.parseInt(pageNumberText);
                        System.out.println("Numer strony: " + pageNumber);
                    }
                    for (int i = 1; i <= pageNumber; i++) {
                        ikeaProductScraper.getProdacts(driver, innerEntry.getValue() + "?page=" + i);
                    }
                }
            }

            int a = 0;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
