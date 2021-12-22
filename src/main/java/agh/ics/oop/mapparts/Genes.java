package agh.ics.oop.mapparts;

import java.util.Arrays;
import java.util.Random;

public class Genes {

    private int[] genes = new int[32];
    private int[] geneTypeCounts = new int[8];

    public Genes()
    {
        Random random = new Random();
        for (int i=0; i < 32; i++)
            genes[i] = random.nextInt(8);
        Arrays.sort(genes);
    }

    public Genes(Animal parent1, Animal parent2)
    {
        Random random = new Random();
        int side = random.nextInt(2);

        if ((side == 0 && parent1.getEnergy() > parent2.getEnergy()) ||
                (side == 1 && parent2.getEnergy() > parent1.getEnergy()))
        {
            int genesFromParent1 = (int) (parent1.getEnergy() / (parent1.getEnergy() + parent1.getEnergy()));
            for (int i=0; i < genesFromParent1; i++)
            {
                genes[i] = parent1.getGenes().getGenes()[i];
                geneTypeCounts[genes[i]] += 1;
            }
            for (int i=genesFromParent1; i < 32; i++)
            {
                genes[i] = parent2.getGenes().getGenes()[i];
                geneTypeCounts[genes[i]] += 1;
            }
        }
    }

    public int[] getGenes()
    {
        return this.genes;
    }

    public int[] getGeneTypeCounts()
    {
        return this.geneTypeCounts;
    }
}
