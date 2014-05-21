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
	
	public Positioncomponent(MTApplication mtApplication)
	{
		super(mtApplication,0,0,0,0,0, 0, 0);
		this.mtApp = mtApplication;
		parent = new MTRectangle(mtApplication, 260, 130);
		parent.unregisterAllInputProcessors();
		parent.setFillColor(MTColor.WHITE);
		parent.setStrokeColor(MTColor.BLACK);	 
	}	
	
	
	public MTRectangle createRectangle(int anzahl)
	{
		this.personenanzahl = anzahl;
		MTRectangle child1;
		MTRectangle child2;
		MTRectangle child3;
		MTRectangle child4;
		switch (anzahl)
		{			
			case 1: //1 Kasten(unten)
				child1 = new MTRectangle(this.mtApp,60,30);
				child1.setPositionRelativeToOther(parent, new Vector3D(130,115));
				child1.unregisterAllInputProcessors();
				child1.setFillColor(MTColor.SILVER);
				child1.setStrokeColor(MTColor.SILVER);
				parent.addChild(child1);
				break;
			case 2: //2 kästen (links und rechts)

				child1 = new MTRectangle(this.mtApp,30,60);
				child1.setPositionRelativeToOther(parent, new Vector3D(15,85));
				child1.unregisterAllInputProcessors();
				child1.setFillColor(MTColor.SILVER);
				child1.setStrokeColor(MTColor.SILVER);
				parent.addChild(child1);

				child2 = new MTRectangle(this.mtApp,30,60);
				child2.setPositionRelativeToOther(parent, new Vector3D(245,85));
				child2.unregisterAllInputProcessors();
				child2.setFillColor(MTColor.SILVER);
				child2.setStrokeColor(MTColor.SILVER);
				parent.addChild(child2);
				break;
			case 3: //3 kästen (links, unten und rechts)
				child1 = new MTRectangle(this.mtApp,60,30);
				child1.setPositionRelativeToOther(parent, new Vector3D(130,115));
				child1.unregisterAllInputProcessors();
				child1.setFillColor(MTColor.SILVER);
				child1.setStrokeColor(MTColor.SILVER);
				parent.addChild(child1);

				child2 = new MTRectangle(this.mtApp,30,60);
				child2.setPositionRelativeToOther(parent, new Vector3D(15,85));
				child2.unregisterAllInputProcessors();
				child2.setFillColor(MTColor.SILVER);
				child2.setStrokeColor(MTColor.SILVER);
				parent.addChild(child2);

				child3 = new MTRectangle(this.mtApp,30,60);
				child3.setPositionRelativeToOther(parent, new Vector3D(245,85));
				child3.unregisterAllInputProcessors();
				child3.setFillColor(MTColor.SILVER);
				child3.setStrokeColor(MTColor.SILVER);
				parent.addChild(child3);

				break;
			case 4: //4 kästen (links, links unten, rechts unten und rechts)
				child1 = new MTRectangle(this.mtApp,30,60);
				child1.setPositionRelativeToOther(parent, new Vector3D(15,65));
				child1.unregisterAllInputProcessors();
				child1.setFillColor(MTColor.SILVER);
				child1.setStrokeColor(MTColor.SILVER);
				parent.addChild(child1);

				child2 = new MTRectangle(this.mtApp,30,60);
				child2.setPositionRelativeToOther(parent, new Vector3D(245,65));
				child2.unregisterAllInputProcessors();
				child2.setFillColor(MTColor.SILVER);
				child2.setStrokeColor(MTColor.SILVER);
				parent.addChild(child2);
				
				child3 = new MTRectangle(this.mtApp,60,30);
				child3.setPositionRelativeToOther(parent, new Vector3D(65,115));
				child3.unregisterAllInputProcessors();
				child3.setFillColor(MTColor.SILVER);
				child3.setStrokeColor(MTColor.SILVER);
				parent.addChild(child3);

				child4 = new MTRectangle(this.mtApp,60,30);
				child4.setPositionRelativeToOther(parent, new Vector3D(195,115));
				child4.unregisterAllInputProcessors();
				child4.setFillColor(MTColor.SILVER);
				child4.setStrokeColor(MTColor.SILVER);
				parent.addChild(child4);
				break;
		}	
		return parent;
	}	
	
	
	public int getPersonenanzahl()
	{
		return this.personenanzahl;
	}
	

}
