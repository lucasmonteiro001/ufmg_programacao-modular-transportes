import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lucas on 5/14/16.
 */
public class MaquinaAviao extends AMaquina {

    private static Integer serieBilhete = 1;

    public MaquinaAviao(Integer numeroDeSerie, Map<String, List<Horario>> destinoHorario, Double valorBilhete,
                        Integer vagasClasseA, Integer vagasClasseB, Integer vagasClasseC, String companhia) {

        this.saldo = 0.0;
        this.numeroDeSerie = numeroDeSerie;
        this.valorDeBilheteUnico = valorBilhete;

        Calendar calobj = Calendar.getInstance();

        Integer month = calobj.get(Calendar.MONTH) + 1,
                day = calobj.get(Calendar.DAY_OF_MONTH),
                year = calobj.get(Calendar.YEAR),
                hour = calobj.get(Calendar.HOUR_OF_DAY),
                minute = calobj.get(Calendar.MINUTE);

        Horario horarioAgora = new Horario(new Tempo(hour, minute), new Data(day,
                month, year), true);

        //Percorre a lista de origem e horarios, e salva no objeto
        for(Map.Entry<String, List<Horario>> Entry: destinoHorario.entrySet()) {

            String destino = Entry.getKey();

            // se nao existe o destino ainda, cria uma nova lista pra ele
            if(!this.bilhetes.containsKey(destino)) {
                this.bilhetes.put(destino, new LinkedList<>()) ;
            }

            // Salva todos os horarios
            for(Horario h : Entry.getValue()) {

                // verifica se o horario da passagem eh menor que o horario de emissao de bilhete, se sim,
                // emite o bilhete para o proximo dia
                if(h.getTempo().getHora() < horarioAgora.getTempo().getHora()) {
                    h.getData().setDia(horarioAgora.getData().getDia() + 1);

                } else if(h.getTempo().getHora() == horarioAgora.getTempo().getHora()) {
                    // se a hora for igual, verifica os minutos
                    if(h.getTempo().getMinutos() <= horarioAgora.getTempo().getMinutos()) {
                        h.getData().setDia(horarioAgora.getData().getDia() + 1);
                    }
                }


                // cria o numero de vagasPorHorario para cada horario
                for(int i = 0; i < vagasClasseA; i++) {

                    Integer numPoltrona = serieBilhete % vagasClasseA;
                    Tempo tempo = new Tempo(h.getTempo().getHora() - 2, h.getTempo().getMinutos());

                    BilheteAviao b = new BilheteAviao(numeroDeSerie, serieBilhete++, destino, horarioAgora, h,
                            "Aeroporto", companhia, valorBilhete, h.hashCode(), "a", numPoltrona, tempo);


                    this.bilhetes.get(destino).add(b);
                }

                // cria o numero de vagasPorHorario para cada horario
                for(int i = 0; i < vagasClasseB; i++) {

                    Integer numPoltrona = serieBilhete % (vagasClasseA + vagasClasseB);
                    Tempo tempo = new Tempo(h.getTempo().getHora() - 2, h.getTempo().getMinutos());

                    BilheteAviao b = new BilheteAviao(numeroDeSerie, serieBilhete++, destino, horarioAgora, h,
                            "Aeroporto", companhia, valorBilhete, h.hashCode(), "b", numPoltrona, tempo);


                    this.bilhetes.get(destino).add(b);
                }

                // cria o numero de vagasPorHorario para cada horario
                for(int i = 0; i < vagasClasseC; i++) {

                    Integer numPoltrona = serieBilhete % (vagasClasseA + vagasClasseB + vagasClasseC);
                    Tempo tempo = new Tempo(h.getTempo().getHora() - 2, h.getTempo().getMinutos());

                    BilheteAviao b = new BilheteAviao(numeroDeSerie, serieBilhete++, destino, horarioAgora, h,
                            "Aeroporto", companhia, valorBilhete, h.hashCode(), "c", numPoltrona, tempo);


                    this.bilhetes.get(destino).add(b);
                }

            }

        }

    }

    public Boolean verificarBilheteDisponivel(String origem, Tempo tempo, String classeBilhete) {

        // verifica se ha algum bilhete com essa origem
        if(bilhetes.get(origem).size() != 0) {

            List<ABilhete> bilhetesDisponiveis = bilhetes.get(origem);

            for(ABilhete bilhete : bilhetesDisponiveis) {

                if(bilhete instanceof BilheteAviao) {

                    BilheteAviao bilheteAviao = (BilheteAviao) bilhete;

                    // verifica se ha algum bilhete disponivel
                    if(bilheteAviao.getClasseVoo().equals(classeBilhete)
                            && (bilhete.embarque.getTempo().toString().equals(tempo.toString()))) {
                        return true;
                    }
                }
                // verifica se ha algum bilhete disponivel
                else if(bilhete.embarque.getTempo().toString().equals(tempo.toString())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String toString() {

        String[] destinos = this.bilhetes.keySet().toString().split(",");

        return "\n" +
                "Maquina para vender passagens aereas na rota " + destinos[0].substring(1) + " - " +
                destinos[1]
                        .substring(0,destinos[1].length() - 1) +
                "\nValor: R$ " + String.format("%.2f", this.valorDeBilheteUnico) +
                "\nhorarios de partida:\n" + this.getHorariosDePartida() +
                "Datas: diariamente" +
                "\n";
    }
}
