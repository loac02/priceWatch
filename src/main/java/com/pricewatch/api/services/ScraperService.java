package com.pricewatch.api.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ScraperService {

    private static final List<String> seletores = List.of(
            "h4.text-secondary-500",         //  KaBuM!
            ".andes-money-amount__fraction", // Mercado Livre
            ".a-price-whole",                // Amazon
            ".price",                        // Genérico
            "[data-selenium=price]",         // Genérico
            ".sales-price",                  // Magalu / Outros
            ".typo-title-large"              // OLX
    );

    public BigDecimal capturarPreco(String url) {
        try {
            //Simula o Navegador para evitar block do site
            Document doc = gerarDoc(url);

            //Localiza o preço dentro da pagina usando os seletores como busca
            Element elementoPreco = localizarPreco(doc,seletores);

            //Normaliza o Preco para envio para o usuario
            return normalizarPreco(elementoPreco);

        } catch (Exception e) {
            System.err.println("[Scraper Erro] Falha ao capturar preço da URL: " + url + " | Erro: " + e.getMessage());
            return null;
        }
    }

    public Document gerarDoc(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .timeout(10000)
                .get();
    }

    public Element localizarPreco (Document doc, List<String> seletores){
        Element elementoPreco = null;
        for (String seletor : seletores) {
            elementoPreco = doc.selectFirst(seletor);
            if (elementoPreco != null) {
                System.out.println("[Scraper] Elemento encontrado usando o seletor: " + seletor);
                break;
            }
        }
        return elementoPreco;
    }

    public BigDecimal normalizarPreco(Element elementoPreco){
        if (elementoPreco != null) {
            String precoTexto = elementoPreco.text();

            precoTexto = precoTexto.replaceAll("\\s+", "");

            precoTexto = precoTexto.replaceAll("[^0-9,,\\.]", "");

            if (precoTexto.contains(",") && precoTexto.contains(".")) {
                precoTexto = precoTexto.replace(".", "").replace(",", ".");
            } else if (precoTexto.contains(",")) {
                precoTexto = precoTexto.replace(",", ".");
            }

            return new BigDecimal(precoTexto).setScale(2, RoundingMode.HALF_UP);
        }
        throw new RuntimeException("Nenhum dos seletores mapeados foi encontrado na página.");
    }
}
