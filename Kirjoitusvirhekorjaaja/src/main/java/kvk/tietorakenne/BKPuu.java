package kvk.tietorakenne;

import kvk.algoritmi.IMuokkausEtaisyyslaskija;
import kvk.enums.Jarjestys;

/**
 * BK-puu tietorakenne, jonka avulla sanastosta voi etsiä nopeasti merkkijonoja,
 * jotka ovat tietyn muokkausetäisyyden sisällä yksittäisestä merkkijonosta.
 * Huomattavaa on, että puulle voi antaa erilaisen etäisyyslaskijan. Pitää
 * kuitenkin muistaa, että etäisyyslaskijan pitää muodostaa sanoille metrinen
 * avaruus.
 */
public class BKPuu {

    private final BKSolmu juuri;
    private IMuokkausEtaisyyslaskija etaisyysLaskija;

    /**
     * @param etaisyysLaskija Määrittää millä tavalla sanojen välinen metrinen
     * etäisyys lasketaan.
     * @param juuriSolmu
     */
    public BKPuu(IMuokkausEtaisyyslaskija etaisyysLaskija, BKSolmu juuriSolmu) {
        this.etaisyysLaskija = etaisyysLaskija;
        this.juuri = juuriSolmu;
    }

    public void asetaEtaisyysLaskija(IMuokkausEtaisyyslaskija laskija) {
        this.etaisyysLaskija = laskija;
    }

    /**
     * Lisää parametrina annetun sanan BK-puuhun. Sana sijoitetaan juuren
     * lapseksi avaimenaan sanan etäisyys juuresta. Jos juurisolmulla on jo
     * lapsi sillä etäisyydellä, siirrytään tähän lapsisolmuun, ja yritetään
     * lisätä sana tämän lapseksi avaimena etäisyys tästä uuden kontekstin
     * solmusta. Tätä valintaa jatketaan kunnes on mahdollista lisätä sana
     * uudeksi lapseksi tietyllä etäisyydellä.
     *
     * @param sana
     */
    public void lisaaSana(String sana) {
        sana = sana.trim().toLowerCase();
        BKSolmu nykyinenSolmu = this.juuri;
        while (true) {
            int etaisyysNykyiseenSolmuun = this.etaisyysLaskija.laskeEtaisyys(nykyinenSolmu.sana, sana);
            if (etaisyysNykyiseenSolmuun == 0) {
                nykyinenSolmu.onPoistettu = false;
                break;
            }
            BKSolmu nykyisenLapsiLisattavanSananEtaisyydella = nykyinenSolmu.lapsiEtaisyydella(etaisyysNykyiseenSolmuun);
            if (nykyisenLapsiLisattavanSananEtaisyydella == null) {
                nykyinenSolmu.lisaaLapsi(new BKSolmu(sana), etaisyysNykyiseenSolmuun);
                break;
            } else {
                nykyinenSolmu = nykyisenLapsiLisattavanSananEtaisyydella;
            }
        }
    }

    /**
     * Poistaa parametrina annetun sanan. Tarkemmin, sanaa ei varsinaisesti
     * poisteta puusta, vaan se merkitään poistetuksi. Poistettavaan solmuun
     * päästään käyttämällä hakua 0 muokkausoperaation toleranssilla.
     *
     * @param sana
     */
    public void poistaSana(String sana) {
        sana = sana.toLowerCase();
        BKSolmu verrattavaSolmu = this.juuri;

        while (true) {
            int etaisyysEtsittavaanSanaan = this.etaisyysLaskija.laskeEtaisyys(sana, verrattavaSolmu.sana);
            if (etaisyysEtsittavaanSanaan == 0) {
                verrattavaSolmu.onPoistettu = true;
                break;
            }
            BKSolmu uusiKandidaatti = verrattavaSolmu.lapsiEtaisyydella(etaisyysEtsittavaanSanaan);
            if (uusiKandidaatti != null) {
                verrattavaSolmu = uusiKandidaatti;
            }

        }
    }

    /**
     * Hakee BK-puusta sanat, jotka ovat muokkausetäisyydeltään lähimpänä
     * parametrina annettua sanaa. Haussa käytetään hyväksi sanojen etäisyyksien
     * välien ominaisuuksia, tarkemmin kolmioepäyhtälöä, jonka perusteella
     * haettavia haaroja voidaan karsia haun nopeuttamiseksi.
     *
     * @param sana jota lähimpänä olevat sanat haetaan.
     * @param etaisyysToleranssi verrattavan sanan maksimietäisyys haettavasta
     * sanasta.
     * @param montaHaetaan
     * @return
     */
    public String[] haeLahimmatSanat(String sana, int etaisyysToleranssi, int montaHaetaan) {
        sana = sana.toLowerCase();
        JarjestyvaTaulukko lahimmatSanat = new JarjestyvaTaulukko(montaHaetaan, Jarjestys.NOUSEVA);
        Pino<BKSolmu> kandidaatit = new Pino<>();

        kandidaatit.lisaa(this.juuri);

        while (!kandidaatit.onTyhja()) {
            BKSolmu verrattavaSolmu = kandidaatit.poista();
            int etaisyysEtsittavaanSanaan = this.etaisyysLaskija.laskeEtaisyys(sana, verrattavaSolmu.sana);
            if (etaisyysEtsittavaanSanaan <= etaisyysToleranssi && !verrattavaSolmu.onPoistettu) {
                lahimmatSanat.lisaa(new SanaEtaisyysPari(verrattavaSolmu.sana, etaisyysEtsittavaanSanaan));
            }
            int rajausAlaRaja = etaisyysEtsittavaanSanaan - etaisyysToleranssi;
            int rajausYlaRaja = etaisyysEtsittavaanSanaan + etaisyysToleranssi;
            for (int i = rajausAlaRaja; i <= rajausYlaRaja; i++) {
                if (i < 0) {
                    continue;
                }
                BKSolmu uusiKandidaatti = verrattavaSolmu.lapsiEtaisyydella(i);
                if (uusiKandidaatti != null) {
                    kandidaatit.lisaa(uusiKandidaatti);
                }
            }
        }

        return lahimmatSanat.haeMerkkijonoTaulukkona();
    }

}
