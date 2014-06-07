package de.braster;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class Positioncomponent extends MTRoundRectangle{
	
	private MTRectangle parent;
	private MTApplication mtApp;
	private int personenanzahl;
	
	public Positioncomponent(MTApplication mtApplication, int anzahl)
	{
		super(mtApplication,0,0,0,0,0, 0, 0);
		this.mtApp = mtApplication;
		parent = new MTRectangle(mtApplication, 160, 100);
		parent.unregisterAllInputProcessors();
		parent.setFillColor(MTColor.WHITE);
		parent.setStrokeColor(MTColor.BLACK);
		
		this.personenanzahl = anzahl;
		MTRectangle child1;
		MTRectangle child2;
		MTRectangle child3;
		MTRectangle child4;
		switch (personenanzahl)
		{			
			case 1: //1 Kasten(unten)
				child1 = new MTRectangle(this.mtApp,50,25);
				child1.setPositionRelativeToOther(parent, new Vector3D(80,87.5f));
				child1.unregisterAllInputProcessors();
				child1.setPickable(false);
				child1.setFillColor(MTColor.SILVER);
				child1.setStrokeColor(MTColor.SILVER);
				parent.addChild(child1);
				break;
			case 2: //2 kästen (links und rechts)

				child1 = new MTRectangle(this.mtApp,25,50);
				child1.setPositionRelativeToOther(parent, new Vector3D(12.5f,60));
				child1.unregisterAllInputProcessors();
				child1.setPickable(false);
				child1.setFillColor(MTColor.SILVER);
				child1.setStrokeColor(MTColor.SILVER);
				parent.addChild(child1);

				child2 = new MTRectangle(this.mtApp,25,50);
				child2.setPositionRelativeToOther(parent, new Vector3D(147.5f,60));
				child2.unregisterAllInputProcessors();
				child2.setPickable(false);
				child2.setFillColor(MTColor.SILVER);
				child2.setStrokeColor(MTColor.SILVER);
				parent.addChild(child2);
				break;
			case 3: //3 kästen (unten, links und rechts)
				child1 = new MTRectangle(this.mtApp,50,25);
				child1.setPositionRelativeToOther(parent, new Vector3D(80,87.5f));
				child1.unregisterAllInputProcessors();
				child1.setPickable(false);
				child1.setFillColor(MTColor.SILVER);
				child1.setStrokeColor(MTColor.SILVER);
				parent.addChild(child1);

				child2 = new MTRectangle(this.mtApp,25,50);
				child2.setPositionRelativeToOther(parent, new Vector3D(12.5f,40));
				child2.unregisterAllInputProcessors();
				child2.setPickable(false);
				child2.setFillColor(MTColor.SILVER);
				child2.setStrokeColor(MTColor.SILVER);
				parent.addChild(child2);

				child3 = new MTRectangle(this.mtApp,25,50);
				child3.setPositionRelativeToOther(parent, new Vector3D(147.5f,40));
				child3.unregisterAllInputProcessors();
				child3.setPickable(false);
				child3.setFillColor(MTColor.SILVER);
				child3.setStrokeColor(MTColor.SILVER);
				parent.addChild(child3);

				break;
			case 4: //4 kästen (links, rechts, unten links und rechts unten)
				child1 = new MTRectangle(this.mtApp,25,50);
				child1.setPositionRelativeToOther(parent, new Vector3D(12.5f,40));
				child1.unregisterAllInputProcessors();
				child1.setPickable(false);
				child1.setFillColor(MTColor.SILVER);
				child1.setStrokeColor(MTColor.SILVER);
				parent.addChild(child1);

				child2 = new MTRectangle(this.mtApp,25,50);
				child2.setPositionRelativeToOther(parent, new Vector3D(147.5f,40));
				child2.unregisterAllInputProcessors();
				child2.setPickable(false);
				child2.setFillColor(MTColor.SILVER);
				child2.setStrokeColor(MTColor.SILVER);
				parent.addChild(child2);
				
				child3 = new MTRectangle(this.mtApp,50,25);
				child3.setPositionRelativeToOther(parent, new Vector3D(40,87.5f));
				child3.unregisterAllInputProcessors();
				child3.setPickable(false);
				child3.setFillColor(MTColor.SILVER);
				child3.setStrokeColor(MTColor.SILVER);
				parent.addChild(child3);

				child4 = new MTRectangle(this.mtApp,50,25);
				child4.setPositionRelativeToOther(parent, new Vector3D(120,87.5f));
				child4.unregisterAllInputProcessors();
				child4.setPickable(false);
				child4.setFillColor(MTColor.SILVER);
				child4.setStrokeColor(MTColor.SILVER);
				parent.addChild(child4);
				break;
		}	
	}	
	
	
	public MTRectangle createRectangle(int anzahl)
	{
		return parent;
	}	
	
	
	public int getPersonenanzahl()
	{
		return this.personenanzahl;
	}
	
	public MTRectangle getRectangle()
	{
		return this.parent;
	}
	 
	

}
