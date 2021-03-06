
package gov.va.vinci.chartreview.model.schema;

// Generated Mar 8, 2011 3:41:01 PM by Hibernate Tools 3.2.4.GA

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.validator.GenericValidator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * AttributeDef generated by hbm2java
 */
@Entity
public class AttributeDef implements java.io.Serializable, Comparable<AttributeDef>
{
    // Attribute types
    static public final int ATTRIBUTE_DEF_TYPE_TEXT = 0;
    static public final int ATTRIBUTE_DEF_TYPE_NUMERIC = 1;
    static public final int ATTRIBUTE_DEF_TYPE_BLOB = 2;
    static public final int ATTRIBUTE_DEF_TYPE_OPTION = 3;
    static public final int ATTRIBUTE_DEF_TYPE_DATE = 4;

    private String id;
    private String name = "New_Attribute";
    private String color = "ffffff"; // White
    private int type = ATTRIBUTE_DEF_TYPE_TEXT;
    private double numericLow = 0.0;
    private double numericHigh = 999999999999.999;
    private Date minDate = new Date(0);
    private Date maxDate = new Date(0);
    private List<AttributeDefOptionDef> attributeDefOptionDefs = new ArrayList<AttributeDefOptionDef>( 0 );
    private AnnotationSchema annotationSchema;
    private List<AttributeDefAttributeDefOptionDefSortOrder> attributeDefAttributeDefOptionDefSortOrders = new ArrayList<AttributeDefAttributeDefOptionDefSortOrder>(0);
    private Timestamp version = new Timestamp((new Date()).getTime());

    public AttributeDef( )
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try
        {
            minDate = sdf.parse("0000-01-01 00:00:00");
            maxDate = sdf.parse("9999-01-01 00:00:00");
        }
        catch(Exception e)
        {
            // Do nothing
        }
    }

    public AttributeDef(AttributeDef obj)
    {
        this(obj, false);
    }

    public AttributeDef(AttributeDef obj, boolean augmentName) {
        this(
                UUID.randomUUID().toString(),
                augmentName ? obj.name + "_copy" : obj.name
        );
        this.color = obj.color;
        this.type = obj.type;
        this.numericLow = obj.numericLow;
        this.numericHigh = obj.numericHigh;
        this.minDate = obj.minDate;
        this.maxDate = obj.maxDate;
        for(Iterator iter = obj.attributeDefOptionDefs.iterator(); iter.hasNext();)
        {
            AttributeDefOptionDef attributeDefOptionDef = (AttributeDefOptionDef)iter.next();
            AttributeDefOptionDef newAttributeDefOptionDef = new AttributeDefOptionDef(attributeDefOptionDef);
            this.addAttributeDefOptionDef(newAttributeDefOptionDef);

            // Find the sort order that belongs to the old option def, fix it up with the new option def, and save it.
            for (Iterator iter2 = obj.attributeDefAttributeDefOptionDefSortOrders.iterator(); iter2.hasNext(); ) {
                AttributeDefAttributeDefOptionDefSortOrder oldAttributeDefAttributeDefOptionDefSortOrder = (AttributeDefAttributeDefOptionDefSortOrder) iter2.next();
                if(oldAttributeDefAttributeDefOptionDefSortOrder.getObjId().compareTo(attributeDefOptionDef.getId()) == 0) {
                    AttributeDefAttributeDefOptionDefSortOrder newSortOrder = new AttributeDefAttributeDefOptionDefSortOrder(oldAttributeDefAttributeDefOptionDefSortOrder);
                    newSortOrder.setObjId(newAttributeDefOptionDef.getId());
                    this.addAttributeDefAttributeDefOptionDefSortOrder(newSortOrder);
                    break;
                }
            }
        }
    }

    public AttributeDef( String uid)
    {
        this.id = uid;
    }

    public AttributeDef( String uid, String name )
    {
        this(uid);
        this.name = name;
    }

    public AttributeDef( String uid, String name, List<AttributeDefOptionDef> attributeDefOptionDefs )
    {
        this(uid);
        this.name = name;
        this.attributeDefOptionDefs = attributeDefOptionDefs;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Column
    public String getName( )
    {
        return this.name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @OneToMany(fetch= FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="attributeDef")
    @OrderBy("name")
    public List<AttributeDefOptionDef> getAttributeDefOptionDefs( )
    {
        return this.attributeDefOptionDefs;
    }

    public List<AttributeDefOptionDef> doGetAttributeDefOptionDefsSorted() {
        return sortAttributeDefOptionDefs(this.attributeDefOptionDefs);
    }

    public List<AttributeDefOptionDef> sortAttributeDefOptionDefs(List<AttributeDefOptionDef> attributeDefOptionDefs) {
        List<AttributeDefOptionDef> attributeDefOptionDefsSorted = new ArrayList<AttributeDefOptionDef>();
        List<AttributeDefAttributeDefOptionDefSortOrder> attributeDefOptionDefSortOrders = new ArrayList<AttributeDefAttributeDefOptionDefSortOrder>();
        List<AttributeDefOptionDef> unsorted = new ArrayList<AttributeDefOptionDef>();
        for(int i = 0; i < attributeDefOptionDefs.size(); i++)
        {
            AttributeDefOptionDef attributeDefOptionDef = attributeDefOptionDefs.get(i);
            AttributeDefAttributeDefOptionDefSortOrder sortOrder = this.findAttributeDefOptionDefSortOrder(attributeDefOptionDef);
            if(sortOrder != null) {
                attributeDefOptionDefSortOrders.add(sortOrder);
            }
            else
            {
                unsorted.add(attributeDefOptionDef);
            }
        }
        Collections.sort(attributeDefOptionDefSortOrders);
        for(int i = 0; i < attributeDefOptionDefSortOrders.size(); i++)
        {
            AttributeDefAttributeDefOptionDefSortOrder sortOrder = attributeDefOptionDefSortOrders.get(i);
            AttributeDefOptionDef attributeDefOptionDef = null;
            for(int j = 0; j < attributeDefOptionDefs.size(); j++) {
                AttributeDefOptionDef tAttributeDefOptionDef = attributeDefOptionDefs.get(j);
                if(tAttributeDefOptionDef.getId().compareTo(sortOrder.getObjId()) == 0)
                {
                    attributeDefOptionDef = tAttributeDefOptionDef;
                    break;
                }
            }
            if(attributeDefOptionDef != null) {
                attributeDefOptionDefsSorted.add(attributeDefOptionDef);
            }
        }
        if(unsorted.size() > 0) {
            attributeDefOptionDefsSorted.addAll(unsorted);
        }
        return attributeDefOptionDefsSorted;
    }

    public void setAttributeDefOptionDefs( List<AttributeDefOptionDef> attributeDefOptionDef )
    {
        this.attributeDefOptionDefs = attributeDefOptionDef;
    }

    public void addAttributeDefOptionDef(AttributeDefOptionDef attributeDefOptionDef) {
        attributeDefOptionDef.setAttributeDef(this);
        this.getAttributeDefOptionDefs().add(attributeDefOptionDef);
    }

    @Column
    public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}


    @Column
    public int getType() {
		return type;
	}
    public void setType(int type) {
		this.type = type;
	}


    @Column
    public double getNumericLow() {
        return numericLow;
    }
    public void setNumericLow(double numericLow) {
        this.numericLow = numericLow;
    }

    @Column
    public double getNumericHigh() {
        return numericHigh;
    }
    public void setNumericHigh(double numericHigh) {
        this.numericHigh = numericHigh;
    }

    @Column
    public Date getMinDate() {
        return minDate;
    }
    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    @Column
    public Date getMaxDate() {
        return maxDate;
    }
    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    @ManyToOne
    public AnnotationSchema getAnnotationSchema() {
        return annotationSchema;
    }

    public void setAnnotationSchema(AnnotationSchema annotationSchema) {
        this.annotationSchema = annotationSchema;
    }

    @Version
    public Timestamp getVersion() {
        return version;
    }

    public void setVersion(Timestamp version) {
        this.version = version;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public int compareTo(AttributeDef compObj) {

        return compare(this, compObj);
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "attributeDef")
    public List<AttributeDefAttributeDefOptionDefSortOrder> getAttributeDefAttributeDefOptionDefSortOrders() {
        return attributeDefAttributeDefOptionDefSortOrders;
    }

    public void clearAttributeDefAttributeDefOptionDefSortOrders() {
        this.attributeDefAttributeDefOptionDefSortOrders.clear();
    }

    public void setAttributeDefAttributeDefOptionDefSortOrders(List<AttributeDefAttributeDefOptionDefSortOrder> attributeDefAttributeDefOptionDefSortOrders) {
        this.attributeDefAttributeDefOptionDefSortOrders = attributeDefAttributeDefOptionDefSortOrders;
    }

    public void addAttributeDefAttributeDefOptionDefSortOrder(AttributeDefAttributeDefOptionDefSortOrder def) {
        def.setAttributeDef(this);
        this.getAttributeDefAttributeDefOptionDefSortOrders().add(def);
    }

    public AttributeDefAttributeDefOptionDefSortOrder findAttributeDefOptionDefSortOrder(AttributeDefOptionDef attributeDefOptionDef)
    {
        AttributeDefAttributeDefOptionDefSortOrder sortOrder = null;
        for(AttributeDefAttributeDefOptionDefSortOrder tSortOrder : this.attributeDefAttributeDefOptionDefSortOrders)
        {
            int results = tSortOrder.getObjId().compareTo(attributeDefOptionDef.getId());
            if(results == 0)
            {
                sortOrder = tSortOrder;
                break;
            }
        }
        return sortOrder;
    }

    @Transient
    public String getTypeSystemFeatureName() {
        if (GenericValidator.isBlankOrNull(name)) {
            return null;
        } else {
            return name.replaceAll("\\s", "").replaceAll("[^a-zA-Z0-9]+", "");
        }
    }
    static int compare(AttributeDef obj1, AttributeDef obj2) {

        String objName1 = obj1.getName();
        String objName2 = obj2.getName();
        return objName1.compareTo(objName2);
    }

    public void sortAttributeDefOptionDefsByIdToOrderMap(Map<String, String> idToOrderMap) {
        Collections.sort(this.attributeDefOptionDefs, new AttributeDefOptionDefComparator(idToOrderMap));
    }

    private static class AttributeDefOptionDefComparator implements Comparator<AttributeDefOptionDef> {
        private Map<String, String> idToOrderMap;

        private AttributeDefOptionDefComparator(Map<String, String> idToOrderMap) {
            this.idToOrderMap = idToOrderMap;
        }

        public int compare(AttributeDefOptionDef o1, AttributeDefOptionDef o2) {
            String id1 = o1.getId();
            String id2 = o2.getId();
            String value1 = (String)idToOrderMap.get(id1);
            String value2 = (String)idToOrderMap.get(id2);
            if(value1 == null)
            {
                return -1;
            }
            if(value2 == null)
            {
                return 1;
            }
            Integer order1 = new Integer(value1);
            Integer order2 = new Integer(value2);
            return order1.compareTo(order2);
        }
    }

}
