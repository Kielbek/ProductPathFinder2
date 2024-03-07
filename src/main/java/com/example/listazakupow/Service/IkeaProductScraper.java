package com.example.listazakupow.Service;

import com.example.listazakupow.Entity.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IkeaProductScraper {
    private final static String URL = "https://www.ikea.com/pl/pl/";

    public Map<String, String> getLink(String divName) throws IOException, InterruptedException {
        Document doc = Jsoup.connect(URL).get();
        Elements carouselSlides = doc.select(divName);
        Map<String, String> productCategoryLink = new HashMap<>();

        for (Element slide : carouselSlides) {
            Element link = slide.selectFirst("a[href]");
            String href = link.attr("href");
            productCategoryLink.put(slide.text(), href);
        }
        return productCategoryLink;
    }

    public Map<String, String> getItemCategoryLink(String linkItem) throws IOException {

        Document doc = Jsoup.connect(linkItem).get();
        Elements linkListItems = doc.select("li.vn-8-grid-gap.vn__link-wrapper");
        Map<String, String> itemCategoryLink = new HashMap<>();

        for (Element slide : linkListItems) {
            Element link = slide.selectFirst("a[href]");
            String href = link.attr("href");
            itemCategoryLink.put(slide.text(), href);
        }
        return itemCategoryLink;
    }


    public void getProdacts(WebDriver driver, String link) throws InterruptedException, IOException {
        String html = driver.getPageSource();
        Document doc = Jsoup.parse(html);
        Elements divs = doc.select("div.plp-fragment-wrapper");

        for (Element priceDiv : divs) {
            Elements div = priceDiv.select("div.pip-product-compact__bottom-wrapper > a");
            String productUrl = div.attr("href");

            Element spanElement = priceDiv.selectFirst("span.notranslate");
            String productName = spanElement.text();

//            Element descriptionElement = priceDiv.selectFirst("div.plp-price-module__description");
//            String productDescription = descriptionElement.text();

            Product product = new Product(productName, productUrl, "productDescription");
            System.out.println(product.getName());
        }


        //productUrl
//        WebElement nameElement = driver.findElement(By.cssSelector("span.pip-header-section__title--big"));
//        WebElement descriptionElement = driver.findElement(By.cssSelector("span.pip-header-section__description-text"));
//        WebElement dimensionsElement = driver.findElement(By.cssSelector("button.pip-header-section__description-measurement"));
//
//        String name = nameElement.getText();
//        String description = descriptionElement.getText();
//        String dimensions = dimensionsElement.getText();
//
//        WebElement button = driver.findElement(By.cssSelector("div.pip-store-section__button.js-click-and-collect-section"));
//        button.click();
//
//        try {
//            List<WebElement> subtitles = driver.findElements(By.cssSelector("span.pip-accordion-item-header__subtitle"));
//
//            for (WebElement subtitle : subtitles) {
//                System.out.println("Subtitle text: " + subtitle);
//                String nazwa = driver.findElement(By.cssSelector("div.pip-store-pick-up-location__caption > span")).getText();
//                String regal = driver.findElement(By.cssSelector("span.pip-product-identifier__value")).getText();
//                String polka = driver.findElement(By.cssSelector("div.pip-store-pick-up-location__bin-container > span.pip-product-identifier__value")).getText();
//
//            }
//        } catch (Exception e) {
//        }


    }

    public Product getProduct(WebDriver driver) {
        WebElement nameElement = driver.findElement(By.cssSelector("span.pip-header-section__title--big"));
        WebElement descriptionElement = driver.findElement(By.cssSelector("span.pip-header-section__description-text"));
        WebElement dimensionsElement = driver.findElement(By.cssSelector("button.pip-header-section__description-measurement"));

        String name = nameElement.getText();
        String description = descriptionElement.getText();
        String dimensions = dimensionsElement.getText();

        return new Product(name, description, dimensions, "nwm", Collections.emptyList());
    }

    public static void setStoreLocation(WebDriver driver, String storeCityPostCode) throws InterruptedException {
        WebElement button = driver.findElement(By.xpath("//span[@class='hnf-btn__label' and text()='Wybierz sklep']"));
        button.click();
        Thread.sleep(3000);
        WebElement inputField = driver.findElement(By.id("hnf-txt-storepicker-postcode"));
        inputField.sendKeys(storeCityPostCode);
        button = driver.findElement(By.xpath("//span[@class='hnf-btn__label' and text()='Znajdź preferowany sklep']"));
        button.click();
        Thread.sleep(5000);
        button = driver.findElement(By.xpath("//span[@class='hnf-btn__label' and text()='Odwiedź stronę sklepu']"));
        button.click();
    }
}
