package edu.ubb.warp.logic;

import java.util.ArrayList;

import edu.ubb.warp.model.Resource;

/**
 * @deprecated
 *
 */
public class ResourceTimeline {
	private Resource resource;
	private ArrayList<Week> timeline;

	public ResourceTimeline() {
	}

	public ArrayList<Week> getTimeline() {
		return timeline;
	}

	public void setTimeline(ArrayList<Week> timeline) {
		this.timeline = timeline;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

}
