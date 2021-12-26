package agh.ics.oop.StatTrackers;

import agh.ics.oop.IAnimalDeathObserver;
import agh.ics.oop.INewAnimalObserver;
import agh.ics.oop.mapparts.Animal;
import agh.ics.oop.mapparts.Genes;
import agh.ics.oop.mapparts.IMapElement;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;

public class GenesStatsTracker implements IAnimalDeathObserver, INewAnimalObserver {

    HashMap<Genes, Integer> genotypeCounter = new HashMap<>();

    public GenesStatsTracker()
    {}

    public void decreaseGenes(Genes genes)
    {
        genotypeCounter.put(genes, genotypeCounter.get(genes) - 1);
        if (genotypeCounter.get(genes) == 0)
            genotypeCounter.remove(genes);
    }

    public void increaseGenes(Genes genes)
    {
        if (genes == null)
            throw new RuntimeException("AAAAAAAAAA");
        if (genotypeCounter.get(genes) == null)
            genotypeCounter.put(genes, 1);
        else
            genotypeCounter.put(genes, genotypeCounter.get(genes) + 1);
    }

    public LinkedList<Genes> getGenesMode()
    {
        LinkedList<Genes> modeGenes = new LinkedList<>();
        int maxValue = 0;
        try {
            for (Genes genes1 : genotypeCounter.keySet()) {
                if (genotypeCounter.get(genes1) > maxValue) {
                    modeGenes.clear();
                    modeGenes.add(genes1);
                    maxValue = genotypeCounter.get(genes1);
                } else if (genotypeCounter.get(genes1) == maxValue) {
                    modeGenes.add(genes1);
                }
            }
        }
        catch (ConcurrentModificationException e){}
        return modeGenes;
    }

    public int getGenesModeCount()
    {
        int genesModeCount = 0;
        // too fast animation may result in concurrent modification exception, can be safely ignored
        try {
            for (Genes genes1 : genotypeCounter.keySet()) {
                if (genotypeCounter.get(genes1) > genesModeCount)
                    genesModeCount = genotypeCounter.get(genes1);
            }
        }
        catch (ConcurrentModificationException e){}
        return genesModeCount;
    }

    @Override
    public void newAnimalPlaced(Animal animal) {
        Genes genes = animal.getGenes();
        this.increaseGenes(genes);
    }

    @Override
    public void animalDied(Animal animal) {
        Genes genes = animal.getGenes();
        this.decreaseGenes(genes);
    }
}
