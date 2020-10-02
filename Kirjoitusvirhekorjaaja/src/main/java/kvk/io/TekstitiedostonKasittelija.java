package kvk.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Hoitaa I/O toimenpiteet.
 */
public class TekstitiedostonKasittelija implements ITekstitiedostonKasittelija {

    private final String sanastoTiedostonNimi = "/sanalista.txt";

    /**
     * Tallentaa String objectissa annetun muuttujan annettuun tiedostoon.
     *
     * @param teksti
     * @param tiedostoPolku
     * @return true jos tekstin tallentaminen tiedostoon onnistui.
     */
    @Override
    public boolean tallennaTeksti(String teksti, File tiedostoPolku) {
        try (BufferedWriter puskuroituKirjoittaja = Files.newBufferedWriter(tiedostoPolku.toPath());) {
            puskuroituKirjoittaja.write(teksti);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Lataa parametrina annetusta tiedostosta tekstin ja palauttaa sen String
     * objektissa.
     *
     * @param tiedostoPolku
     * @return tiedostosta ladattu teksti.
     * @throws IOException
     */
    @Override
    public String lataaTeksti(File tiedostoPolku) throws IOException {
        try (BufferedReader puskuroituLukija = Files.newBufferedReader(tiedostoPolku.toPath())) {

            String teksti = "";
            String rivi;

            while ((rivi = puskuroituLukija.readLine()) != null) {
                teksti = teksti + rivi;
            }
            return teksti;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Lataa resursseista korjaajan käyttämän sanaston listana String objekteja.
     *
     * @return Resursseissa sijaitseva sanasto string listana.
     * @throws IOException
     */
    @Override
    public List<String> lataaSanastoListana() throws IOException {
        List<String> sanalista = new ArrayList<>();
        try (BufferedReader puskuroituLukija = new BufferedReader(new InputStreamReader(TekstitiedostonKasittelija.class.getResourceAsStream(this.sanastoTiedostonNimi)))) {

            String rivi;

            while ((rivi = puskuroituLukija.readLine()) != null) {
                sanalista.add(rivi);
            }
            return sanalista;
        } catch (IOException e) {
            throw e;
        }
    }
    
    /**
     * Satunnaisotanta kaikista sanaston sanoista käyttäen reservoir sampling algoritmia.
     * @param otosKoko
     * @return otos listana sanoja
     * @throws IOException
     */
    public String[] lataaSatunnainenOtosSanoja(int otosKoko) throws IOException {
        String[] otos = new String[otosKoko];
        
        try (BufferedReader puskuroituLukija = new BufferedReader(new InputStreamReader(TekstitiedostonKasittelija.class.getResourceAsStream(this.sanastoTiedostonNimi)))) {

            String rivi;
            int indeksi = 0;
            Random satunnaisGeneraattori = new Random(System.nanoTime());

            while ((rivi = puskuroituLukija.readLine()) != null) {
                if (indeksi < otosKoko) {
                    otos[indeksi] = rivi;
                } else {
                    int randomIndex = satunnaisGeneraattori.nextInt(indeksi);
                    if (randomIndex < otosKoko) {
                        otos[randomIndex] = rivi;
                    }
                }
                indeksi += 1;
            }
            
        } catch (IOException e) {
            throw e;
        }
        return otos;
    }

}
