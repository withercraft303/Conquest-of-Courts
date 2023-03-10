package GameAssets;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;

import java.awt.Graphics;

public class UnitManager {
    // referance vars
    private Camera cam;
    EnemyManager enemies;

    private int idCounter = 0; //t assuming we will not have over 2^32 units spawned in a single game
    TreeSet<Unit> allUnits = new TreeSet<>(new DrawOrder());
    HashMap<Integer, Unit> unitById = new HashMap<Integer, Unit>();
    LinkedList<UnitAttacker> attackingUnits = new LinkedList<UnitAttacker>();
    LinkedList<UnitDefender> defendingUnits = new LinkedList<UnitDefender>();

    public void setReferances(Camera c, EnemyManager e){
        this.cam = c;
        this.enemies = e;
    }
    public void update(){
        // TODO split this into updating by attacking units and defending units
        for (AttackField a: enemies.attacks()){
            for (Unit u: allUnits){
                u.collides(a);
            }
        }
        for (Unit u: allUnits){
            u.update();
            u.move();
            if (u.isDead()){
                allUnits.remove(u);
                unitById.remove(u.getId());
                if (u instanceof UnitDefender){
                    defendingUnits.remove(u);
                } else if (u instanceof UnitAttacker){
                    attackingUnits.remove(u);
                }
                break;
            }
        }
    }
    public void spawn(Unit u){
        allUnits.add(u);
        unitById.put(idCounter, u);
        u.setId(idCounter);
        idCounter++;
        if (u instanceof UnitAttacker){
            attackingUnits.add((UnitAttacker)u);
        } else if (u instanceof UnitDefender){
            defendingUnits.add((UnitDefender)u);
        }
    }
    public Unit clicked(int mouseX, int mouseY){
        for (Unit u: allUnits){
            if (u.distTo(mouseX, mouseY) < Math.max(u.height, u.width)/2){
                return u;
            }
        }
        return null;
    }
    public void draw(Graphics g){
        for (Unit u: allUnits){
            if (cam.onScreen((int)u.xCenter(), (int)u.yCenter(), u.height)){
                u.draw(cam.anchorX(), cam.anchorY(), g);
            }
        }
    }

    public String toString(){
        String output = " #";
        for(Unit u: attackingUnits){
            output = output + u.toString(); 
        }
        if (attackingUnits.size() == 0){
            output = output + " none";
        }
        output = output + " #";
        for(Unit u: defendingUnits){
            output =  output + u.toString();
        }
        if (defendingUnits.size() == 0){
            output = output + " none";
        }

        return output;
    }

    private class DrawOrder implements Comparator<Unit>{

        @Override
        public int compare(Unit unitA, Unit unitB) {
            return (int)(unitA.y() - unitB.y());
        }
        
    }
}
