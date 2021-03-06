<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
        <r:require modules="bootstrap"/>
        <g:if test="${!readOnly}">
            <g:title>Add Process to Project Wizard</g:title>
        </g:if>
        <g:else>
            <g:title>Review Process</g:title>
        </g:else>
	</head>
	<body>
        <style>
            input[disabled], textarea[readonly="readonly"], textarea[readonly], input[readonly="readonly"], select[readonly="readonly"], select[disabled]
            {
                color: #000000;
                opacity: 50;
            }
            .ui-state-disabled {
                color: #000000;
            }
        </style>
		<div id="create-dataSetConfiguration" class="content scaffold-create" role="main">
            <g:if test="${!readOnly}">
                <legend>Step 3 - Users and Clinical Elements In This Process</legend>
            </g:if>
            <g:else>
                <legend>Review Process - Users and Clinical Elements In This Process</legend>
            </g:else>
            <g:render template="/templates/showErrors" model="[model: "${dataSetConfigurationInstance}"]" />
            <g:form class="form-horizontal" >
                <fieldset class="form" id="formFields">
                    <legend>Task Creation Query</legend>
                    <div>
                        Enter the query to create process tasks.
                        <i class="icon-question-sign" rel="tooltip" title="Query should return a single column, the principal clinical element id." id="assignmentToolTip"></i>
                    </div>
                    <textarea name="query" style="width: 100%; height: 200px" >${model.query}</textarea>
                    <br/><br/>
                    <legend>Users</legend>
                    <div style="margin: 20px; margin-bottom: 60px;">
                        <g:each in="${authorities}" var="user">
                            <div class="form-horizontal" style="margin: 5px"><g:checkBox name="processUsers" value="${user.user.username}" checked="${model.processUsers.contains(user.user.username)}"/> ${user.user.username}</div>
                        </g:each>
                    </div>
                    <legend>Other</legend>
                    <div class="control-group">
                        <label for="processOrTask" class="control-label">
                            Assignment style
                        <i class="icon-question-sign" rel="tooltip" title="If by process, an entire process is assigned to a single user. If by task, then tasks within a single process may be performed by different users." id="assignmentToolTip"></i>
                        </label>
                        <div class="controls">
                            <g:select class="form-control" id="processOrTask" name="processOrTask"
                                      from="${[[k:'process', v:'By Process'], [k:'task', v:'By Task']] }"
                                      value="${model.processOrTask}"
                                      optionKey="k" optionValue="v" />
                        </div>
                    </div>
                    <br/><br/><br/>
                </fieldset>
                <g:submitButton name="previous" value="Previous" class="btn btn-primary" style="float:left"/>
                <g:if test="${!readOnly}">
                    <g:submitButton name="finish" value="Create Tasks and Finish" class="btn btn-primary" style="float:right"/>
                </g:if>
                <g:else>
                    <g:link controller="project" action="show" id="${project.id}" style="float: right;" class="btn btn-primary">Back To Project</g:link>
                </g:else>
            </g:form>
		</div>
        <script>
            $(document).ready(function(){
                $("[rel=tooltip]").tooltip({ placement: 'right'});
            });
        </script>
        <g:if test="${readOnly}">
            <script src="${request.contextPath}/js/jquery-disabler.min.js"></script>
            <script>
                $(function() {

                    $("#formFields").disabler({
                        disable : true
                    });

                    $("#formFields").disabler("readOnly", "formFields", true);

                });
            </script>
        </g:if>
	</body>
</html>
