Ext.define('CR.app.view.ChartReviewPanel', {
    extend: 'Ext.panel.Panel',
//    layout: 'fit',
    renderTo: "appContent",
    alias: 'widget.chartreview',
    id: "appContainer",
    autoCreateViewPort: false,
    plugins: [
        {
            ptype: 'crfittoparent'
        }],
    mixins: {
        annotationaware: 'CR.app.controller.AnnotationNatureController'
    },

    statics: {

        numClinicalElementPortlets : new Array(),

        addPortalColumn2IfNecessary: function()
        {
            var appPortals = Ext.ComponentQuery.query('component[id="app-portal"]');
            var appPortal = appPortals[0];
            var col2s = Ext.ComponentQuery.query('component[id="app-portal-col-2"]');
            var col2 = col2s[0];
            if(!col2)
            {
                col2 = {
                    id: 'app-portal-col-2',
                    type: 'portalcolumn',
                    items:[]
                };
                appPortal.add(col2);
            }
        },
        removePortalColumn2IfPossible: function()
        {
            var appPortals = Ext.ComponentQuery.query('component[id="app-portal"]');
            var appPortal = appPortals[0];
            var col2s = Ext.ComponentQuery.query('component[id="app-portal-col-2"]');
            var col2 = col2s[0];
            if(col2)
            {
                appPortal.remove(col2);
            }
        },
        refreshClinicalElementConfigurations: function()
        {
            // Tell all the clinical element views the defs changed
            // Any view that cares about clinicalElementConfigurationId
            var comps = Ext.ComponentQuery.query('[clinicalElementConfigurationId]');
            for(var key1 in comps)
            {
                var comp = comps[key1];
                comp.fireEvent("clinicalElementConfigurationsChanged");
            }
        }
    },

    listeners: {
        principalClinicalElementLoaded: function()
        {
            // By this point, the clinical element configurations have been loaded.
            this.updateForNewClinicalElementConfigurations();

            // The task is principal clinical element (task) is loaded, so we can create
            // clinical element views for it.
            this.createTaskClinicalElementPortlets();
        },
        annotationSelectedByUserInList: function()
        {
            this.showSelectedAnnotation();
        },
        render: function(v)
        {
            if(!this.annotationAwareInitialized)
            {
                this.initAnnotationAwareness();
            }
        }
    },

    constructor: function(config) {
        this.callParent(config);

        // Needed for loading tasks
        CR.app.controller.AnnotationNatureController.projectId = CR.app.model.CRAppData.projectId;
        CR.app.controller.AnnotationNatureController.processId = CR.app.model.CRAppData.processId;
        CR.app.controller.AnnotationNatureController.taskId = CR.app.model.CRAppData.taskId;
        CR.app.controller.AnnotationNatureController.taskType = CR.app.model.CRAppData.taskType;
        this.addEvents('schemaLoaded');
    },

    getTools: function(){
        return [{
            xtype: 'tool',
            type: 'gear',
            handler: function(e, target, header, tool){
                var portlet = header.ownerCt;
                portlet.setLoading('Loading...');
                Ext.defer(function() {
                    portlet.setLoading(false);
                }, 2000);
            }
        }];
    },

    initComponent: function(){
        Ext.apply(this,
            {
                id: 'app-viewport',
                layout:
                {
                    type: 'border'
//                    padding: '5 5 5 5',
//                    margins: '5 5 5 5'
                },
                items:
                    [
                        {
                            id: 'annotationcontrolpanel',
                            xtype: 'container',
                            region: 'center',
                            layout:
                            {
                                type: 'border'
//                                padding: '5 5 5 5',
//                                margins: '5 5 5 5'
                            },
                            items:
                                [
                                    {
                                        id: 'annotationpanel',
                                        title: 'Annotation',
                                        xtype: 'panel',
                                        region: 'west',
                                        width: '30%',
                                        height: '100%',
                                        split: true,
                                        collapsible: true,
                                        layout:
                                        {
                                            type: 'border',
                                            padding: '5 5 5 5'
//                                            margins: '5 5 5 5'
                                        },
                                        items:
                                            [
                                                {
                                                    id: 'taskspanel',
                                                    title: 'Task',
                                                    xtype: 'panel',
                                                    width: '100%',
                                                    height: '15%',
                                                    region: 'north',
                                                    split: false,
                                                    layout:
                                                    {
                                                        type: 'border',
                                                        padding: '5 5 5 5'
//                                                        margins: '5 5 5 5'
                                                    },
                                                    items:
                                                        [
                                                            {
                                                                id: 'taskpanel',
                                                                xtype: 'taskpanel',
                                                                itemId: 'CR.app.view.AnnotationTaskPanel',
                                                                region: 'north'
                                                            }
                                                            ,{
                                                                id: 'tasklistpanel',
                                                                xtype: 'tasklist',
                                                                margins: '5 5 0 5',
                                                                itemId: 'CR.app.view.AnnotationTaskList',
                                                                region: 'center'
                                                            }
                                                        ]
                                                },
                                                {
                                                    id: 'annotationspanel',
                                                    title: 'Annotations',
                                                    xtype: 'panel',
                                                    itemId: 'CR.app.view.AnnotationsPanel',
                                                    region: 'center',
                                                    layout:
                                                    {
                                                        type: 'border',
                                                        padding: '5 5 5 5',
                                                        margins: '5 5 5 5'
                                                    },
                                                    split: true,
                                                    collapsible: false,
                                                    height: '85%',
                                                    width: '100%',
                                                    items:
                                                        [
                                                            {
                                                                id: 'schemapanel',
                                                                xtype: 'schemapanel',
                                                                itemId: 'CR.app.view.AnnotationSchemaPanel',
                                                                region: 'north',
                                                                split: true,
                                                                width: '100%',
                                                                height: '25%'
                                                            }
                                                            ,{
                                                            xtype: 'panel',
                                                            layout:
                                                            {
                                                                type: 'border'
//                                                                padding: '5 5 5 5',
//                                                                margins: '5 5 5 5'
                                                            },
                                                            region: 'center',
                                                            split: true,
                                                            height: '25%',
                                                            width: '100%',
                                                            items:
                                                                [
                                                                    {
                                                                        xtype: 'annotationlistbuttontoolbar',
                                                                        id: 'annotationlistbuttontoolbar',
                                                                        itemId: 'CR.app.view.AnnotationListButtonToolbar',
                                                                        region: 'north'
                                                                    }
                                                                    ,{
                                                                    id: 'annotationlist',
                                                                    xtype: 'annotationlist',
                                                                    itemId: 'CR.app.view.AnnotationList',
                                                                    region: 'center',
                                                                    split: true,
                                                                    height: '100%',
                                                                    width: '100%'
                                                                }
                                                                ]
                                                        }
                                                            ,
                                                            {
                                                                xtype: 'annotationeditor',
                                                                id: 'annotationeditor',
                                                                itemId: 'CR.app.view.AnnotationEditor',
                                                                region: 'south',
                                                                split: true,
                                                                height: '33%',
                                                                width: '100%'
                                                            }
                                                        ]
                                                }
                                            ]
                                    },
                                    {
                                        id: 'app-container',
                                        xtype: 'container',
                                        region: 'center',
                                        layout: {
                                            type: 'border'
                                        },
                                        items:
                                            [
                                                {
                                                    id: 'app-header',
                                                    xtype: 'toolbar',
                                                    width: '100%',
                                                    region: 'north',
                                                    layout: {
                                                        type: 'hbox',
                                                        pack: 'end'
                                                    },
                                                    items:
                                                        [
                                                            {
                                                                id: 'app-header-column-button',
                                                                xtype: 'button',
                                                                icon: 'images/column-double-icon_small.png',
                                                                tooltip: 'Single column, double column toggle',
                                                                enableToggle: true,
                                                                handler: this.onNumPortalColumnsChange
                                                            },
                                                            {
                                                                id: 'app-header-clinical-view-menu',
                                                                xtype: 'button',
                                                                text : 'Add View',
                                                                tooltip: 'Add a clinical view of the current patient',
                                                                menu:[
                                                                    {
                                                                    }
                                                                ]
                                                            }
//                                                            ,
//                                                            {
//                                                                id: 'clinical-element-defs-button',
//                                                                xtype: 'button',
//                                                                icon: 'images/refresh.gif',
//                                                                tooltip: 'Refresh clinical view definitions',
//                                                                handler: function() {
//                                                                    CR.app.view.ChartReviewPanel.refreshClinicalElementConfigurations();
//                                                                }
//                                                            }
                                                        ]
                                                },
                                                {
                                                id: 'app-portal',
                                                xtype: 'portalpanel',
                                                region: 'center',
                                                items:
                                                    [
                                                        {
                                                            id: 'app-portal-col-1',
                                                            type: 'portalcolumn'
                                                        }
                                                    ]
                                            }
                                            ]
                                    }
                                ]
                        }
                    ]
            }
        );
        this.callParent(arguments);
    },

    updateForNewClinicalElementConfigurations: function()
    {
        this.updateAddClinicalElementViewMenu();
        //this.createDefaultPortlets(); INITIAL DEBUGGING - get from Task selection now.
    },

    updateAddClinicalElementViewMenu: function()
    {
        var menuItems = [];
        var selectedPrincipalClinicalElement = CR.app.controller.AnnotationNatureController.selectedPrincipalClinicalElement;
        if(selectedPrincipalClinicalElement)
        {
            for(var key1 in CR.app.model.CRAppData.clinicalElementConfigurations)
            {
                var clinicalElementConfiguration = CR.app.model.CRAppData.clinicalElementConfigurations[key1];
                var clinicalElementConfigurationId = clinicalElementConfiguration.dataIndex;
                var found = false;
                for(var key2 in selectedPrincipalClinicalElement.contextElementTypes)
                {
                    var contextClinicalElementType = selectedPrincipalClinicalElement.contextElementTypes[key2];
                    if(contextClinicalElementType.typeId == clinicalElementConfigurationId)
                    {
                        found = true;
                    }
                }
                if(found)
                {
                    var me = this;
                    var menuItem =
                    {
                        text: clinicalElementConfiguration.text,
                        itemId: clinicalElementConfigurationId,
                        handler:function(btn, item)
                        {
                            var clinicalElementConfiguration = me.getClinicalElementConfigurationByName(btn.text)
                            me.addPortlet(clinicalElementConfiguration.dataIndex, false);
                        }
                    }
                    menuItems.push(menuItem);
                }
            }
        }

        var comps = Ext.ComponentQuery.query('[id=app-header-clinical-view-menu]');
        for(var key1 in comps)
        {
            var comp = comps[key1];
            var newMenu = Ext.create('Ext.menu.Menu', {
                plain: true,
                items: menuItems
            });
            comp.menu = newMenu;
        }
    },

    showMsg: function(msg) {
        var el = Ext.get('app-msg'),
            msgId = Ext.id();

        this.msgId = msgId;
        el.update(msg).show();

        Ext.defer(this.clearMsg, 3000, this, [msgId]);
    },

    clearMsg: function(msgId) {
        if (msgId === this.msgId) {
            Ext.get('app-msg').hide();
        }
    },

    onNumPortalColumnsChange: function(btn) {
        var col1 = Ext.getCmp('app-portal-col-1');
        var col1portals = col1.items.items;
        var col2 = Ext.getCmp('app-portal-col-2');
        if(col2)
        {
            // move column 2 portals to column 1
            var col2portals = col2.items.items;
            var col2portalsToMove = new Array();
            for (key in col2portals)
            {
                var col2portal = col2portals[key];
                col2portalsToMove.push(col2portal);
            }
            for (key in col2portalsToMove)
            {
                var col2portal = col2portalsToMove[key];
                // Note: add removes from old parent and adds to new parent
                col1.add(col2portal);
            }

            // Remove the column
            CR.app.view.ChartReviewPanel.removePortalColumn2IfPossible()

            // Change button icon to double-column
            var btn = Ext.getCmp('app-header-column-button');
            btn.setIcon('images/column-double-icon_small.png');
        }
        else
        {
            // Change button icon to single-column
            var btn = Ext.getCmp('app-header-column-button');
            btn.setIcon('images/column-single-icon_small.png');

            // Add the column
            CR.app.view.ChartReviewPanel.addPortalColumn2IfNecessary();
            col2 = Ext.getCmp('app-portal-col-2');
            // move half of column 1 portals to column 2
            var numCol1Portals = col1portals.length;
            if(numCol1Portals > 1)
            {
                var col1portalsToMove = new Array();
                for (var i = Math.round(numCol1Portals/2); i < numCol1Portals; i++)
                {
                    var col1portal = col1portals[i];
                    col1portalsToMove.push(col1portal);
                }
                for (key in col1portalsToMove)
                {
                    var col1portal = col1portalsToMove[key];
                    // Note: add removes from old parent and adds to new parent
                    col2.add(col1portal);
                }
            }
        }
    },

    getPortletName: function(clinicalElementConfigurationId)
    {
        var clinicalElementConfiguration = this.getClinicalElementConfiguration(clinicalElementConfigurationId);
        var portletName = clinicalElementConfiguration.text;
        var numPortlets = CR.app.view.ChartReviewPanel.numClinicalElementPortlets[clinicalElementConfigurationId];
        CR.app.view.ChartReviewPanel.numClinicalElementPortlets[clinicalElementConfigurationId] = numPortlets + 1;
        portletName += " " + numPortlets;
        return portletName;
    },

    getPortletType: function(clinicalElementConfigurationId)
    {
        // Always this - no differentiation here.
        var type = 'CR.app.view.ClinicalElementPortlet';
        return type;
    },

    getPortletsByType: function(clinicalElementConfigurationId)
    {
        var portlets = [];
        var existingPortlets = Ext.ComponentQuery.query('[objectType=portlet]');
        if(existingPortlets.length > 0)
        {
            for(var i = 0; i < existingPortlets.length; i++)
            {
                var tPortlet = existingPortlets[i];
                if(tPortlet && tPortlet.clinicalElementConfigurationId == clinicalElementConfigurationId)
                {
                    portlets.push(tPortlet);
                }
            }
        }
        return portlets;
    },

    getFirstPortletByType: function(clinicalElementConfigurationId)
    {
        var portlet = null;
        var existingPortlets = Ext.ComponentQuery.query('[objectType=portlet]');
        if(existingPortlets.length > 0)
        {
            for(var i = 0; i < existingPortlets.length; i++)
            {
                var tPortlet = existingPortlets[i];
                if(tPortlet && tPortlet.clinicalElementConfigurationId == clinicalElementConfigurationId)
                {
                    portlet = tPortlet;
                    break;
                }
            }
        }
        return portlet;
    },

    addPortlet: function(clinicalElementConfigurationId, ifNotFound) {
        var col1s = Ext.ComponentQuery.query('component[id="app-portal-col-1"]');
        var col1 = col1s[0];
        var col1portals = col1.items.items;
        var col2s = Ext.ComponentQuery.query('component[id="app-portal-col-2"]');
        var col2 = col2s[0];
        var portletType = this.getPortletType(clinicalElementConfigurationId);
        var comps1 = Ext.ComponentQuery.query('[clinicalElementConfigurationId]');
        var portlet = this.getFirstPortletByType(clinicalElementConfigurationId);
        if(!ifNotFound || ifNotFound && !portlet)
        {
            portlet = Ext.create(portletType);
            portlet.objectType = 'portlet' ;
            portlet.clinicalElementConfigurationId = clinicalElementConfigurationId;

            var clinicalElementConfiguration = this.getClinicalElementConfiguration(clinicalElementConfigurationId);
            portlet.title = clinicalElementConfiguration.text;
            portlet.setClinicalElementConfigurationId(clinicalElementConfigurationId);

//            // Drag and drop ability
//            var dd = new Ext.dd.DD(portlet, 'portletsDDGroup', {
//                isTarget  : false
//            });
//            //Apply the overrides object to the newly created instance of DD
//            Ext.apply(dd, overrides);
//            portlet.addToGroup('portletsDDGroup');
//            var portletDDTarget = new Ext.dd.DDTarget('portlet', 'portletsDDGroup');

            var portletAdded = false;
            if(col2)
            {
                var col2portals = col2.items.items;
                if(col2portals.length < col1portals.length)
                {
                    col2.add(portlet);
                    portletAdded = true;
                }
            }
            if(!portletAdded)
            {
                col1.add(portlet);
            }
        }
        return portlet;
    },

    closeAllPortlets: function()
    {
        var col1 = Ext.getCmp('app-portal-col-1');
        var col2 = Ext.getCmp('app-portal-col-2');
        if(col2)
        {
            var col2portals = col2.items.items;
            var col2portalsToRemove = new Array();
            for (key in col2portals)
            {
                var col2portal = col2portals[key];
                col2portalsToRemove.push(col2portal);
            }
            for (key in col2portalsToRemove)
            {
                var col2portal = col2portalsToRemove[key];
                col2.remove(col2portal);
                col2portal.destroy();
            }
        }
        if(col1)
        {
            var col1portals = col1.items.items;
            var col1portalsToRemove = new Array();
            for (key in col1portals)
            {
                var col1portal = col1portals[key];
                col1portalsToRemove.push(col1portal);
            }
            for (key in col1portalsToRemove)
            {
                var col1portal = col1portalsToRemove[key];
                col1.remove(col1portal);
                col1portal.destroy();
            }
        }
    },

    // Debug - Create one portlet for each type of clinical element in the configurations
    // loaded in this session.
    createDefaultPortlets: function()
    {
        // Check to see if there are already clinical elements views displayed.
        // Create some if there are none.
        var comps1 = Ext.ComponentQuery.query('[clinicalElementConfigurationId]');
        if(comps1.length <= 0)
        {

            // Create one clinical element views of each clinical element type
            // specified in the definition.
            var isFirst = true;
            for(var key1 in CR.app.model.CRAppData.clinicalElementConfigurations)
            {
                var clinicalElementConfiguration = CR.app.model.CRAppData.clinicalElementConfigurations[key1];
                var clinicalElementConfigurationId = clinicalElementConfiguration.dataIndex;
                var portlet = this.addPortlet(clinicalElementConfigurationId, true);

                //Collapse all but the first view.
                if(isFirst)
                {
                    isFirst = false;
                }
                else
                {
//                    portlet.collapsed = true;
                }
//                if(!isFirst) // DEBUG STOPS ALL But first
//                {
//                    break;
//                }
            }
        }
        var appPortal = Ext.getCmp('app-portal');
        var body = appPortal.body;
        body.scrollTo('top', 0);
    },

    createTaskClinicalElementPortlets: function()
    {
        var selectedPrincipalClinicalElement = CR.app.controller.AnnotationNatureController.selectedPrincipalClinicalElement;
        if(selectedPrincipalClinicalElement)
        {
            var contextElementTypes = selectedPrincipalClinicalElement['contextElementTypes'];

            // Sort the context element types by sequence so that we display the clincial element portlets
            // in the order specified by the user for this task task.
            contextElementTypes.sort(function(contextElementTypeNode1, contextElementTypeNode2){
                var sequence1 = parseInt(contextElementTypeNode1.sequence);
                var sequence2 = parseInt(contextElementTypeNode2.sequence);
                return sequence1 > sequence2;
            });

            if(contextElementTypes)
            {
                // Assume that the previously selected principal clinical element and currently selected principal clinical
                // element have different context element types, requiring a change of portlets, because there may not have
                // been a previously selected principal clinical element...
                var changePortlets = true;

                var previouslySelectedPrincipalClinicalElement = CR.app.controller.AnnotationNatureController.previouslySelectedPrincipalClinicalElement;

                if(previouslySelectedPrincipalClinicalElement)
                {
                    var previousContextElementTypes = previouslySelectedPrincipalClinicalElement['contextElementTypes'];

                    // Sort the context element types by sequence so that we compare the clincial element portlets
                    // in the order specified by the user for this task task.
                    previousContextElementTypes.sort(function(contextElementTypeNode1, contextElementTypeNode2){
                        var sequence1 = parseInt(contextElementTypeNode1.sequence);
                        var sequence2 = parseInt(contextElementTypeNode2.sequence);
                        return sequence1 > sequence2;
                    });

                    if(previousContextElementTypes)
                    {
                        var same = true;
                        if (contextElementTypes.length != previousContextElementTypes.length) {
                            same = false;
                        }
                        else {
                            for (var i = 0; i < contextElementTypes.length; i++) {
                                if (contextElementTypes[i].typeId != previousContextElementTypes[i].typeId) {
                                    same = false;
                                    break;
                                }
                            }
                        }
                        if (same) {
                            changePortlets = false;
                        }
                    }
                }
                if(changePortlets)
                {
                    var portlet = null;

                    // Find existing portlets that are not of the type needed so that we can delete them.
                    // Also remove the context element types for which we already have an existing portlet.
                    var existingPortlets = Ext.ComponentQuery.query('[objectType=portlet]');
                    var found = false;
                    var portletsToDelete = [];
                    if(existingPortlets.length > 0)
                    {
                        for(var i = 0; i < existingPortlets.length; i++)
                        {
                            var tPortlet = existingPortlets[i];
                            for(key in contextElementTypes)
                            {
                                var contextElementType = contextElementTypes[key];
                                if(contextElementType && contextElementType.hidden == "false")
                                {
                                    var clinicalElementConfigurationId = contextElementType.typeId; // Expecting the clinical element configuration id
                                    if(clinicalElementConfigurationId)
                                    {
                                        if(tPortlet && tPortlet.clinicalElementConfigurationId == clinicalElementConfigurationId)
                                        {
                                            found = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if(!found)
                            {
                                // Delete this portlet, it will not be used in this task.
                                portletsToDelete.push(tPortlet);
                            }
                            else
                            {
                                // Remove the context element type; we already have a portlet of this type.
                                var pos = contextElementTypes.indexOf(tPortlet.clinicalElementConfigurationId);
                                if(pos >= 0)
                                {
                                    contextElementTypes.splice(pos, 1);
                                }

                                // Tell that portlet to refresh it's data with the new selected
                                // principal clinical element.
                                tPortlet.updateData();
                            }
                        }
                    }
                    // Look in both columns for the portlets to delete.
                    var col1 = Ext.getCmp('app-portal-col-1');
                    var col2 = Ext.getCmp('app-portal-col-2');
                    for(var i = 0; i < portletsToDelete.length; i++)
                    {
                        var tPortlet = portletsToDelete[i];
                        var removed = false;
                        if(col2)
                        {
                            var col2portals = col2.items.items;
                            for (key in col2portals)
                            {
                                var col2portal = col2portals[key];
                                if(col2portal == tPortlet)
                                {
                                    col2.remove(col2portal);
                                    col2portal.destroy();
                                    removed = true;
                                    break;
                                }
                            }
                        }
                        if(col1 && !removed)
                        {
                            var col1portals = col1.items.items;
                            for (key in col1portals)
                            {
                                var col1portal = col1portals[key];
                                if(col1portal == tPortlet)
                                {
                                    col1.remove(col1portal);
                                    col1portal.destroy();
                                    removed = true;
                                    break;
                                }
                            }
                        }

                    }
                    // NOTE: This will add the context element types that we do not have an existing portal for, AFTER
                    // the existing portals.  If the user is getting another principal clinical element for the same
                    // type of task that he just finished, this ordering will be BRADBRAD
                    for(key in contextElementTypes)
                    {
                        var contextElementType = contextElementTypes[key];
                        if(contextElementType && contextElementType.hidden == "false")
                        {
                            var clinicalElementConfigurationId = contextElementType.typeId; // Expecting the clinical element configuration id
//                      var allowAnnotationNode = contextElementTypeNode.getElementsByTagName('allowAnnotation')[0];
//                      var contextElementAllowAnnotation = CR.app.controller.AnnotationNatureControllerText.getTextValue(allowAnnotationNode);  // TBD - need to turn off annotation with this - maybe a flag in AnnotationAwareness...
                            if(clinicalElementConfigurationId)
                            {
                                var portlet = this.addPortlet(clinicalElementConfigurationId, true);
                            }
                        }
                    }
                }
                else if(CR.app.controller.AnnotationNatureController.previouslySelectedPrincipalClinicalElement.id != CR.app.controller.AnnotationNatureController.selectedPrincipalClinicalElement.id)
                {
                    var existingPortlets = Ext.ComponentQuery.query('[objectType=portlet]');
                    for (var i = 0; i < existingPortlets.length; i++)
                    {
                        var tPortlet = existingPortlets[i];

                        // Tell all portlets to refresh their data with the new selected
                        // principal clinical element.
                        tPortlet.updateData();
                    }
                }
            }
        }
    },

    getClinicalElementConfiguration: function(clinicalElementConfigurationId)
    {
        var clinicalElementConfiguration = null;
        for(key in CR.app.model.CRAppData.clinicalElementConfigurations)
        {
            var tClinicalElementConfiguration = CR.app.model.CRAppData.clinicalElementConfigurations[key];
            if(tClinicalElementConfiguration.dataIndex == clinicalElementConfigurationId)
            {
                clinicalElementConfiguration = tClinicalElementConfiguration;
                break;
            }
        }
        return clinicalElementConfiguration;
    },

    getClinicalElementConfigurationByName: function(clinicalElementConfigurationName)
    {
        var clinicalElementConfiguration = null;
        for(key in CR.app.model.CRAppData.clinicalElementConfigurations)
        {
            var tClinicalElementConfiguration = CR.app.model.CRAppData.clinicalElementConfigurations[key];
            if(tClinicalElementConfiguration.text == clinicalElementConfigurationName)
            {
                clinicalElementConfiguration = tClinicalElementConfiguration;
                break;
            }
        }
        return clinicalElementConfiguration;
    },

    showSelectedAnnotation: function()
    {
        var selectedAnnotation = CR.app.controller.AnnotationNatureController.getSelectedAnnotation();
        if(selectedAnnotation != null && selectedAnnotation.clinicalElementConfigurationId)
        {
            var portlets = this.getPortletsByType(selectedAnnotation.clinicalElementConfigurationId);
            if(portlets.length <= 0)
            {
                this.addPortlet(selectedAnnotation.clinicalElementConfigurationId, true);
            }
            else
            {
                for(var i = portlets.length - 1; i >= 0; i--)
                {
                    var portlet = portlets[i];
                    portlet.showSelectedAnnotation();
                }
            }
        }
    }
});
