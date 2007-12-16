package edu.columbia.threescompany.graphics;

import edu.columbia.threescompany.common.Coordinate;
import edu.columbia.threescompany.gameobjects.Blob;
import edu.columbia.threescompany.gameobjects.DeathRayBlob;
import edu.columbia.threescompany.gameobjects.ExplodingBlob;
import edu.columbia.threescompany.gameobjects.ForceBlob;
import edu.columbia.threescompany.gameobjects.GameParameters;
import edu.columbia.threescompany.gameobjects.SlipperyBlob;

public class ActionPointEngine {
	
	public static double getCostOfPhysicalMove(Blob blob, Coordinate click) {
		return GameParameters.BASE_PHYSICAL_MOVE_COST + (blob.getPosition().distanceFrom(click) * blob.getWeight() * 0.5);
	}
	
	public static double getCostOfProratedAction(Blob blob) {
		double ret;
		if (blob instanceof ForceBlob) {
			ret = GameParameters.BASE_FORCE_COST + (blob.getWeight() / GameParameters.BASE_FORCE_COST);   
		} else if (blob instanceof ExplodingBlob) {
			ret = GameParameters.BASE_EXPLODE_COST + (blob.getWeight() / GameParameters.BASE_EXPLODE_COST);
		} else {
			throw new RuntimeException("can only perform actions on blobs");
		}
		
		return ret;
	}
	
	public static double getCostOfSplit(Blob blob) {
		return GameParameters.BASE_SPLIT_COST;
	}
	
	public static double getCostOfProjectile(Blob blob) {
		if (!(blob instanceof DeathRayBlob) && !(blob instanceof SlipperyBlob)) {
			throw new RuntimeException("projectile costs only apply to death ray and slippery blobs");
		}
		return (blob instanceof DeathRayBlob) ? GameParameters.BASE_FIRE_DEATH_RAY_COST : GameParameters.BASE_FIRE_SLIPPERY_COST;	
	}
}
