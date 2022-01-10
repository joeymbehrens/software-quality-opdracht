public class test {
    String[] infoTekst={"--1--","--2--","--3--","--4--","leeg"};
    int[] aankomsttijden = new int[5];

    public void setRegels() {
		int aantalRegels = 0;
        if(!infoBordRegels.isEmpty()) {
            for  (String busID : infoBordRegels.keySet()) {
                updateBoard(busID, aantalRegels);
            }
        }
    }

    public void updateBoard(String busID, int aantalRegels){
        JSONBericht regel = infoBordRegels.get(busID);
        int dezeTijd=regel.getAankomsttijd();
        String dezeTekst=regel.getInfoRegel();
        int plaats=aantalRegels;
        
        for (int i = aantalRegels; i > 0; i--){
            if (dezeTijd < aankomsttijden[i - 1]){
                aankomsttijden[i] = aankomsttijden[i - 1];
                infoTekst[i] = infoTekst[i - 1];
                plaats = i - 1;
            }
        }
        aankomsttijden[plaats] = dezeTijd;
        infoTekst[plaats] = dezeTekst;
        if (aantalRegels < 4){
            aantalRegels++;
        }

        if(checkRepaint(aantalRegels, aankomsttijden)){
			repaintInfoBord(infoTekst);
		}
    }
}