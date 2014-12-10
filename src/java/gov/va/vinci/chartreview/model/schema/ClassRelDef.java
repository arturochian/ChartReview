package gov.va.vinci.chartreview.model.schema;

// Generated Mar 8, 2011 3:41:01 PM by Hibernate Tools 3.2.4.GA


import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

/**
 * ClassRelDef generated by hbm2java
 */
@Entity
public class ClassRelDef  implements java.io.Serializable, Comparable<ClassRelDef> {
    private String id;
    private String name = "New_Class_Relationship";
    private String color = "ffff00"; // Yellow
    private int type;
    private List<AttributeDef> attributeDefs = new ArrayList<AttributeDef>(0);
    private List<ClassDef> leftClassDefs = new ArrayList<ClassDef>(0);
    private List<ClassDef> rightClassDefs = new ArrayList<ClassDef>(0);
    private AnnotationSchema annotationSchema;
    private List<ClassRelDefAttributeDefSortOrder> attributeDefSortOrders = new ArrayList<ClassRelDefAttributeDefSortOrder>(0);
    private List<ClassRelDefLeftClassDefSortOrder> leftClassDefSortOrders = new ArrayList<ClassRelDefLeftClassDefSortOrder>(0);
    private List<ClassRelDefRightClassDefSortOrder> rightClassDefSortOrders = new ArrayList<ClassRelDefRightClassDefSortOrder>(0);
    private Timestamp version = new Timestamp((new Date()).getTime());

	public ClassRelDef() {
	}

    public ClassRelDef(ClassRelDef obj, Map<AttributeDef, AttributeDef> attributeDefMap, Map<ClassDef, ClassDef> classDefMap) {
        this(obj, attributeDefMap, classDefMap, false);
    }

    public ClassRelDef(ClassRelDef obj, Map<AttributeDef, AttributeDef> attributeDefMap, Map<ClassDef, ClassDef> classDefMap, boolean augmentName) {
        this(
                UUID.randomUUID().toString(),
                augmentName ? obj.name + "_copy" : obj.name
        );
        this.color = obj.color;
        this.type = obj.type;
        for(Iterator iter = obj.attributeDefs.iterator(); iter.hasNext();)
        {
            AttributeDef attributeDef = (AttributeDef)iter.next();
            AttributeDef newAttributeDef = attributeDefMap.get(attributeDef);
            this.addAttributeDef(newAttributeDef);
            for (Iterator iter2 = obj.attributeDefSortOrders.iterator(); iter2.hasNext(); ) {
                ClassRelDefAttributeDefSortOrder sortOrder = (ClassRelDefAttributeDefSortOrder) iter2.next();
                if(attributeDef.getId().compareTo(sortOrder.getObjId()) == 0) {
                    ClassRelDefAttributeDefSortOrder newSortOrder = new ClassRelDefAttributeDefSortOrder(sortOrder);
                    newSortOrder.setObjId(newAttributeDef.getId());
                    this.addAttributeDefSortOrder(newSortOrder);
                }
            }
        }
        for(Iterator iter = obj.leftClassDefs.iterator(); iter.hasNext();)
        {
            ClassDef classDef = (ClassDef)iter.next();
            ClassDef newClassDef = classDefMap.get(classDef);
            this.addLeftClassDef(newClassDef);
            for (Iterator iter2 = obj.leftClassDefSortOrders.iterator(); iter2.hasNext(); ) {
                ClassRelDefLeftClassDefSortOrder sortOrder = (ClassRelDefLeftClassDefSortOrder) iter2.next();
                if(classDef.getId().compareTo(sortOrder.getObjId()) == 0) {
                    ClassRelDefLeftClassDefSortOrder newSortOrder = new ClassRelDefLeftClassDefSortOrder(sortOrder);
                    newSortOrder.setObjId(newClassDef.getId());
                    this.addLeftClassDefSortOrder(newSortOrder);
                }
            }
        }
        for(Iterator iter = obj.rightClassDefs.iterator(); iter.hasNext();)
        {
            ClassDef classDef = (ClassDef)iter.next();
            ClassDef newClassDef = classDefMap.get(classDef);
            this.addRightClassDef(newClassDef);
            for (Iterator iter2 = obj.rightClassDefSortOrders.iterator(); iter2.hasNext(); ) {
                ClassRelDefRightClassDefSortOrder sortOrder = (ClassRelDefRightClassDefSortOrder) iter2.next();
                if(classDef.getId().compareTo(sortOrder.getObjId()) == 0) {
                    ClassRelDefRightClassDefSortOrder newSortOrder = new ClassRelDefRightClassDefSortOrder(sortOrder);
                    newSortOrder.setObjId(newClassDef.getId());
                    this.addRightClassDefSortOrder(newSortOrder);
                }
            }
        }
    }

    public ClassRelDef(String uid, String name) {
        this.id = uid;
        this.name = name;
	}

	public ClassRelDef(String uid, String name, List<ClassDef> leftClassDefs, List<ClassDef> rightClassDefs, int type, List<AttributeDef> attributeDefs)
    {
	    this(uid, name);
        this.leftClassDefs = leftClassDefs;
        this.rightClassDefs = rightClassDefs;
        this.type = type;
        this.attributeDefs = attributeDefs;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

    @ManyToOne
    public AnnotationSchema getAnnotationSchema() {
        return annotationSchema;
    }

    public void setAnnotationSchema(AnnotationSchema annotationSchema) {
        this.annotationSchema = annotationSchema;
    }

    @OneToMany
    @OrderBy("name")
    public List<ClassDef> getLeftClassDefs() {
        return this.leftClassDefs;
    }

    public List<ClassDef> doGetLeftClassDefsSorted() {
        return sortLeftClassDefs(this.leftClassDefs);
    }

    public List<ClassDef> sortLeftClassDefs(List<ClassDef> leftClassDefs) {
        List<ClassDef> classDefsSorted = new ArrayList<ClassDef>();
        List<ClassRelDefLeftClassDefSortOrder> classDefSortOrders = new ArrayList<ClassRelDefLeftClassDefSortOrder>();
        List<ClassDef> unsorted = new ArrayList<ClassDef>();
        for(int i = 0; i < leftClassDefs.size(); i++)
        {
            ClassDef classDef = leftClassDefs.get(i);
            ClassRelDefLeftClassDefSortOrder sortOrder = this.findLeftClassDefSortOrder(classDef);
            if(sortOrder != null) {
                classDefSortOrders.add(sortOrder);
            }
            else
            {
                unsorted.add(classDef);
            }
        }
        Collections.sort(classDefSortOrders);
        for(int i = 0; i < classDefSortOrders.size(); i++)
        {
            ClassRelDefLeftClassDefSortOrder sortOrder = classDefSortOrders.get(i);
            ClassDef classDef = null;
            for(int j = 0; j < leftClassDefs.size(); j++) {
                ClassDef tClassDef = leftClassDefs.get(j);
                if(tClassDef.getId().compareTo(sortOrder.getObjId()) == 0)
                {
                    classDef = tClassDef;
                    break;
                }
            }
            if(classDef != null) {
                classDefsSorted.add(classDef);
            }
        }
        if(unsorted.size() > 0) {
            classDefsSorted.addAll(unsorted);
        }
        return classDefsSorted;
    }

    public void setLeftClassDefs(List<ClassDef> leftClassDefs) {
        this.leftClassDefs = leftClassDefs;
    }

    public void addLeftClassDef(ClassDef def) {
        // These get set when these defs are added to their parents.
//        def.setAnnotationSchema(this.annotationSchema);
//        def.setParent(this);
        this.getLeftClassDefs().add(def);
    }

    @OneToMany
    @OrderBy("name")
    public List<ClassDef> getRightClassDefs() {
        return this.rightClassDefs;
    }

    public List<ClassDef> doGetRightClassDefsSorted() {
        return sortRightClassDefs(this.rightClassDefs);
    }

    public List<ClassDef> sortRightClassDefs(List<ClassDef> rightClassDefs) {
        List<ClassDef> classDefsSorted = new ArrayList<ClassDef>();
        List<ClassRelDefRightClassDefSortOrder> classDefSortOrders = new ArrayList<ClassRelDefRightClassDefSortOrder>();
        List<ClassDef> unsorted = new ArrayList<ClassDef>();
        for(int i = 0; i < rightClassDefs.size(); i++)
        {
            ClassDef classDef = rightClassDefs.get(i);
            ClassRelDefRightClassDefSortOrder sortOrder = this.findRightClassDefSortOrder(classDef);
            if(sortOrder != null) {
                classDefSortOrders.add(sortOrder);
            }
            else
            {
                unsorted.add(classDef);
            }
        }
        Collections.sort(classDefSortOrders);
        for(int i = 0; i < classDefSortOrders.size(); i++)
        {
            ClassRelDefRightClassDefSortOrder sortOrder = classDefSortOrders.get(i);
            ClassDef classDef = null;
            for(int j = 0; j < rightClassDefs.size(); j++) {
                ClassDef tClassDef = rightClassDefs.get(j);
                if(tClassDef.getId().compareTo(sortOrder.getObjId()) == 0)
                {
                    classDef = tClassDef;
                    break;
                }
            }
            if(classDef != null) {
                classDefsSorted.add(classDef);
            }
        }
        if(unsorted.size() > 0) {
            classDefsSorted.addAll(unsorted);
        }
        return classDefsSorted;
    }

    public void setRightClassDefs(List<ClassDef> rightClassDefs) {
        this.rightClassDefs = rightClassDefs;
    }

    public void addRightClassDef(ClassDef def) {
        // These get set when these defs are added to their parents.
//        def.setAnnotationSchema(this.annotationSchema);
//        def.setParent(this);
        this.getRightClassDefs().add(def);
    }

    @ManyToMany
    @OrderBy("name")
    public List<AttributeDef> getAttributeDefs() {
        return this.attributeDefs;
    }

    public List<AttributeDef> doGetAttributeDefsSorted() {
        return sortAttributeDefs(this.attributeDefs);
    }

    public List<AttributeDef> sortAttributeDefs(List<AttributeDef> attributeDefs) {
        List<AttributeDef> attributeDefsSorted = new ArrayList<AttributeDef>();
        List<ClassRelDefAttributeDefSortOrder> attributeDefSortOrders = new ArrayList<ClassRelDefAttributeDefSortOrder>();
        List<AttributeDef> unsorted = new ArrayList<AttributeDef>();
        for(int i = 0; i < attributeDefs.size(); i++)
        {
            AttributeDef attributeDef = attributeDefs.get(i);
            ClassRelDefAttributeDefSortOrder sortOrder = this.findAttributeDefSortOrder(attributeDef);
            if(sortOrder != null) {
                attributeDefSortOrders.add(sortOrder);
            }
            else
            {
                unsorted.add(attributeDef);
            }
        }
        Collections.sort(attributeDefSortOrders);
        for(int i = 0; i < attributeDefSortOrders.size(); i++)
        {
            ClassRelDefAttributeDefSortOrder sortOrder = attributeDefSortOrders.get(i);
            AttributeDef attributeDef = null;
            for(int j = 0; j < attributeDefs.size(); j++) {
                AttributeDef tAttributeDef = attributeDefs.get(j);
                if(tAttributeDef.getId().compareTo(sortOrder.getObjId()) == 0)
                {
                    attributeDef = tAttributeDef;
                    break;
                }
            }
            if(attributeDef != null) {
                attributeDefsSorted.add(attributeDef);
            }
        }
        if(unsorted.size() > 0) {
            attributeDefsSorted.addAll(unsorted);
        }
        return attributeDefsSorted;
    }

    public void setAttributeDefs(List<AttributeDef> attributeDefs) {
        this.attributeDefs = attributeDefs;
    }

    public void addAttributeDef(AttributeDef def) {
        this.getAttributeDefs().add(def);
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "classRelDef")
    public List<ClassRelDefAttributeDefSortOrder> getAttributeDefSortOrders() {
        return attributeDefSortOrders;
    }

    public void clearAttributeDefSortOrders() {
        this.attributeDefSortOrders.clear();
    }

    public void setAttributeDefSortOrders(List<ClassRelDefAttributeDefSortOrder> attributeDefSortOrders) {
        this.attributeDefSortOrders = attributeDefSortOrders;
    }

    public void addAttributeDefSortOrder(ClassRelDefAttributeDefSortOrder def) {
        def.setClassRelDef(this);
        this.getAttributeDefSortOrders().add(def);
    }

    public ClassRelDefAttributeDefSortOrder findAttributeDefSortOrder(AttributeDef attributeDef)
    {
        ClassRelDefAttributeDefSortOrder sortOrder = null;
        for(ClassRelDefAttributeDefSortOrder tSortOrder : this.attributeDefSortOrders)
        {
            int results = tSortOrder.getObjId().compareTo(attributeDef.getId());
            if(results == 0)
            {
                sortOrder = tSortOrder;
                break;
            }
        }
        return sortOrder;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "classRelDef")
    public List<ClassRelDefLeftClassDefSortOrder> getLeftClassDefSortOrders() {
        return leftClassDefSortOrders;
    }

    public void clearLeftClassDefSortOrders() {
        this.leftClassDefSortOrders.clear();
    }

    public void setLeftClassDefSortOrders(List<ClassRelDefLeftClassDefSortOrder> leftClassDefSortOrders) {
        this.leftClassDefSortOrders = leftClassDefSortOrders;
    }

    public void addLeftClassDefSortOrder(ClassRelDefLeftClassDefSortOrder def) {
        def.setClassRelDef(this);
        this.getLeftClassDefSortOrders().add(def);
    }

    public ClassRelDefLeftClassDefSortOrder findLeftClassDefSortOrder(ClassDef ClassDef)
    {
        ClassRelDefLeftClassDefSortOrder sortOrder = null;
        for(ClassRelDefLeftClassDefSortOrder tSortOrder : this.leftClassDefSortOrders)
        {
            int results = tSortOrder.getObjId().compareTo(ClassDef.getId());
            if(results == 0)
            {
                sortOrder = tSortOrder;
                break;
            }
        }
        return sortOrder;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "classRelDef")
    public List<ClassRelDefRightClassDefSortOrder> getRightClassDefSortOrders() {
        return rightClassDefSortOrders;
    }

    public void clearRightClassDefSortOrders() {
        this.rightClassDefSortOrders.clear();
    }

    public void setRightClassDefSortOrders(List<ClassRelDefRightClassDefSortOrder> rightClassDefSortOrders) {
        this.rightClassDefSortOrders = rightClassDefSortOrders;
    }

    public void addRightClassDefSortOrder(ClassRelDefRightClassDefSortOrder def) {
        def.setClassRelDef(this);
        this.getRightClassDefSortOrders().add(def);
    }

    public ClassRelDefRightClassDefSortOrder findRightClassDefSortOrder(ClassDef ClassDef)
    {
        ClassRelDefRightClassDefSortOrder sortOrder = null;
        for(ClassRelDefRightClassDefSortOrder tSortOrder : this.rightClassDefSortOrders)
        {
            int results = tSortOrder.getObjId().compareTo(ClassDef.getId());
            if(results == 0)
            {
                sortOrder = tSortOrder;
                break;
            }
        }
        return sortOrder;
    }

    @Column
    public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
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

    public int compareTo(ClassRelDef compObj) {

        return compare(this, compObj);
    }

    static int compare(ClassRelDef obj1, ClassRelDef obj2) {

        String objName1 = obj1.getName();
        String objName2 = obj2.getName();
        return objName1.compareTo(objName2);
    }

    public void sortAttributeDefsByIdToOrderMap(Map<String, String> idToOrderMap) {
        Collections.sort(this.attributeDefs, new AttributeDefComparator(idToOrderMap));
    }

    private static class AttributeDefComparator implements Comparator<AttributeDef> {
        private Map<String, String> idToOrderMap;

        private AttributeDefComparator(Map<String, String> idToOrderMap) {
            this.idToOrderMap = idToOrderMap;
        }

        public int compare(AttributeDef o1, AttributeDef o2) {
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

    public void sortLeftClassDefsByIdToOrderMap(Map<String, String> idToOrderMap) {
        Collections.sort(this.leftClassDefs, new ClassDefComparator(idToOrderMap));
    }

    public void sortRightClassDefsByIdToOrderMap(Map<String, String> idToOrderMap) {
        Collections.sort(this.rightClassDefs, new ClassDefComparator(idToOrderMap));
    }

    private static class ClassDefComparator implements Comparator<ClassDef> {
        private Map<String, String> idToOrderMap;

        private ClassDefComparator(Map<String, String> idToOrderMap) {
            this.idToOrderMap = idToOrderMap;
        }

        public int compare(ClassDef o1, ClassDef o2) {
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