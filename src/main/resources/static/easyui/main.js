      $.get("http://localhost:8080/api/v1/database/list", function(data, status){
        data.entities.forEach(obj=>{
            $("#conSelect").append("<option value=\""+obj.uniqueName+"\">"+obj.dbTitle+"</option>");
        });
      });

      var tableMetaData;
      $('#conSelect').on('change', function() {
        console.log( this.value );
        $('#tableSelect')
            .empty()
            .append('<option selected>Select Tables</option>');
        $.get("http://localhost:8080/api/v1/database/init/"+this.value , function(data, status){
            tableMetaData=data;
            data.tableList.forEach(obj=>{
                $("#tableSelect").append("<option value=\""+obj+"\">"+obj+"</option>");
            });
        });
      });

      $('#tableSelect').on('change', function() {
           console.log( this.value );
           let myColumnList = [];
           tableMetaData.tableInfo.forEach(obj=>{
               if(obj.tableName==this.value){
                   console.log( obj.columnName );
                   myColumnList[obj.ordinalPosition-1]=obj.columnName;
               }
           });
           console.log(myColumnList);
           var tableId="table_ViewData";
           var tableHtml="";
           $("#table_ViewData").empty();
           $("#createForm").empty();
           //$("#tableView").append("<table class=\"table table-bordered table-light table-hover table-success table-striped\" id=\""+tableId+"\"></table>");
           tableHtml+="<thead><tr>";
           myColumnList.forEach(column => {
                tableHtml+="<th>"+column+"</th>";
           });
           tableHtml+="<th>ACTION</th>";
           tableHtml+="</tr></thead>";
           tableHtml+="<tbody>";
           $.get("http://localhost:8080/api/v1/table/list/"+$("#conSelect").val()+"/"+this.value , function(data, status){
               data.data.forEach(obj=>{
                   tableHtml+="<tr>";
                   myColumnList.forEach(column => {
                        tableHtml+="<td>"+obj[column]+"</td>";
                   });
                   tableHtml+="<td><input type=\"button\" onclick='createEditForm("+obj['id']+")' class=\"btn btn-primary\" value=\"Edit\"><input type=\"button\" onclick='createDeleteForm("+obj['id']+")' class=\"btn btn-danger\" value=\"Delete\"></td>";
                   tableHtml+="</tr>";
               });
               tableHtml+="</tbody>";
              //console.log(tableHtml);
              $("#"+tableId).append(tableHtml);
           });
      });

      $("#createNewRecordButton").on('click',function(){
             $("#table_ViewData").empty();
             $("#createForm").empty();
             $('#createForm').attr("action", "http://localhost:8080/api/v1/table/create/"+$("#conSelect").val()+"/"+$("#tableSelect").val());
             $.get("http://localhost:8080/api/v1/table/form/"+$("#conSelect").val()+"/"+$("#tableSelect").val() ,
                function(data, status){
                var tableFormMetaData=data;
                tableFormMetaData.forEach(obj=>{
                    if(obj.dataType==="varchar"){
                        var label="<label for=\""+obj.columnName+"\" class=\"form-label\">"+obj.columnName+"</label>";
                        var inputElement="<input type='text' class=\"form-control\"  id='"+obj.columnName+"' name='"+obj.columnName+"'/>"
                        $("#createForm").append("<div class=\"row mb-3\"><div class=\"col col-3\">"+label+"</div><div class=\"col col-3\">"+inputElement+"</div></div>");
                    }
                    if(obj.dataType==="tinyint"){
                        var label="<label for=\""+obj.columnName+"\" class=\"form-label\">"+obj.columnName+"</label>";
                        var inputElement="<input type='number' class=\"form-control\" id='"+obj.columnName+"' name='"+obj.columnName+"'/>"
                        $("#createForm").append("<div class=\"row mb-3\"><div class=\"col col-3\">"+label+"</div><div class=\"col col-3\">"+inputElement+"</div></div>");
                    }
                    if(obj.dataType==="date"){
                        var label="<label for=\""+obj.columnName+"\" class=\"form-label\">"+obj.columnName+"</label>";
                        var inputElement="<input type='date' class=\"form-control\" id='"+obj.columnName+"' name='"+obj.columnName+"'/>"
                        $("#createForm").append("<div class=\"row mb-3\"><div class=\"col col-3\">"+label+"</div><div class=\"col col-3\">"+inputElement+"</div></div>");
                    }
                    if(obj.dataType==="bigint" && obj.isForeignKey===true){
                        var label="<label for=\""+obj.columnName+"\" class=\"form-label\">"+obj.columnName+"</label>";
                        var inputElement="<select class=\"form-control\" id='"+obj.columnName+"' name='"+obj.columnName+"'/>"
                        $("#createForm").append("<div class=\"row mb-3\"><div class=\"col col-3\">"+label+"</div><div class=\"col col-3\">"+inputElement+"</div></div>");

                        var fkTableFormMetaData;
                        $.get("http://localhost:8080/api/v1/table/form/"+$("#conSelect").val()+"/"+obj.foreignKeyTableName, function(data, status){
                                        fkTableFormMetaData=data;
                                        console.log(fkTableFormMetaData);
                                        //preparing only required columns for the list excludes id and dates
                        });
                        $.get("http://localhost:8080/api/v1/table/list/"+$("#conSelect").val()+"/"+obj.foreignKeyTableName, function(data1, status1){
                           $("#"+obj.columnName).append("<option value=\"\">Select From "+obj.foreignKeyTableName+"</option>");
                           console.log(data1);
                           data1.data.forEach(e=>{
                                var valueOfThis="";
                                fkTableFormMetaData.forEach(e1=>{
                                      valueOfThis += e[e1.columnName]+", "; //showing only required values in list
                                });
                                $("#"+obj.columnName).append("<option value=\""+e.id+"\">"+valueOfThis+"</option>");
                           });
                        });
                    }
                });
                var inputElementBack="<input type='button' class=\"btn btn-secondary form-control\"  id='"+$("#tableSelect").val()+"_FormCancel' value='Cancel & Back' name='"+$("#tableSelect").val()+"_FormCancel'/>"
                var inputElement="<input type='button' class=\"btn btn-primary form-control\"  id='"+$("#tableSelect").val()+"_FormSubmit' value='Submit Form' name='"+$("#tableSelect").val()+"_FormSubmit'/>"
                $("#createForm").append("<div class=\"row mb-3\"><div class=\"col col-3\">"+inputElementBack+"</div><div class=\"col col-3\">"+inputElement+"</div></div>");
                $('#'+$("#tableSelect").val()+'_FormCancel').on('click', function() {
                    $('#tableSelect').trigger('change');
                });
                $('#'+$("#tableSelect").val()+'_FormSubmit').on('click', function() {
                                                    // Serialize form data to JSON
                    var formData = {};
                    $.each($('#createForm').serializeArray(), function(i, field) {
                      formData[field.name] = field.value;
                    });
                    console.log(formData);

                    // Send the data using AJAX
                    $.ajax({
                      type: 'POST',
                      url: $('#createForm').attr('action'), // Form action URL
                      data: JSON.stringify(formData),
                      contentType: 'application/json',
                      success: function(response) {
                        // Handle success response
                        console.log('Success:', response);
                        $('#tableSelect').trigger('change');
                      },
                      error: function(error) {
                        // Handle error response
                        console.error('Error:', error);
                      }
                    });
              });

             });

      });

      function createEditForm(id){
             console.log('Edit Form for '+id);
             $("#table_ViewData").empty();
             $("#createForm").empty();
             var editObjectValues;
             $.ajaxSetup({async: false});
             $.get("http://localhost:8080/api/v1/table/view/"+$("#conSelect").val()+"/"+$("#tableSelect").val()+"/"+id ,
                             function(data, status){
                                editObjectValues=data;
                console.log("editObjectValues 1 --> "+JSON.stringify(editObjectValues) );
                             });
             if(editObjectValues===undefined){
                                console.log("empty 1 --> "+JSON.stringify(editObjectValues) );
             }
             $('#createForm').attr("action", "http://localhost:8080/api/v1/table/edit/"+$("#conSelect").val()+"/"+$("#tableSelect").val());
             $.get("http://localhost:8080/api/v1/table/form/"+$("#conSelect").val()+"/"+$("#tableSelect").val() ,
                function(data, status){
                console.log("editObjectValues 2 --> "+editObjectValues );
                var tableFormMetaData=data;
                tableFormMetaData.forEach(obj=>{
                    if(obj.dataType==="varchar"){
                        var label="<label for=\""+obj.columnName+"\" class=\"form-label\">"+obj.columnName+"</label>";
                        var inputElement="<input type='text' class=\"form-control\" value='"+editObjectValues[obj.columnName]+"'  id='"+obj.columnName+"' name='"+obj.columnName+"'/>"
                        $("#createForm").append("<div class=\"row mb-3\"><div class=\"col col-3\">"+label+"</div><div class=\"col col-3\">"+inputElement+"</div></div>");
                    }
                    if(obj.dataType==="tinyint"){
                        var label="<label for=\""+obj.columnName+"\" class=\"form-label\">"+obj.columnName+"</label>";
                        var inputElement="<input type='number' class=\"form-control\" value='"+editObjectValues[obj.columnName]+"' id='"+obj.columnName+"' name='"+obj.columnName+"'/>"
                        $("#createForm").append("<div class=\"row mb-3\"><div class=\"col col-3\">"+label+"</div><div class=\"col col-3\">"+inputElement+"</div></div>");
                    }
                    if(obj.dataType==="date"){
                        var label="<label for=\""+obj.columnName+"\" class=\"form-label\">"+obj.columnName+"</label>";
                        var inputElement="<input type='date' class=\"form-control\" value='"+editObjectValues[obj.columnName]+"' id='"+obj.columnName+"' name='"+obj.columnName+"'/>"
                        $("#createForm").append("<div class=\"row mb-3\"><div class=\"col col-3\">"+label+"</div><div class=\"col col-3\">"+inputElement+"</div></div>");
                    }
                    if(obj.dataType==="bigint" && obj.isForeignKey===true){
                        var label="<label for=\""+obj.columnName+"\" class=\"form-label\">"+obj.columnName+"</label>";
                        var inputElement="<select class=\"form-control\" id='"+obj.columnName+"' name='"+obj.columnName+"'/>"
                        $("#createForm").append("<div class=\"row mb-3\"><div class=\"col col-3\">"+label+"</div><div class=\"col col-3\">"+inputElement+"</div></div>");

                        var fkTableFormMetaData;
                        $.get("http://localhost:8080/api/v1/table/form/"+$("#conSelect").val()+"/"+obj.foreignKeyTableName, function(data, status){
                                        fkTableFormMetaData=data;
                                        console.log(fkTableFormMetaData);
                                        //preparing only required columns for the list excludes id and dates
                        });
                        $.get("http://localhost:8080/api/v1/table/list/"+$("#conSelect").val()+"/"+obj.foreignKeyTableName, function(data1, status1){
                           $("#"+obj.columnName).append("<option value=\"\">Select From "+obj.foreignKeyTableName+"</option>");
                           console.log(data1);
                           data1.data.forEach(e=>{
                                var valueOfThis="";
                                fkTableFormMetaData.forEach(e1=>{
                                      valueOfThis += e[e1.columnName]+", "; //showing only required values in list
                                });
                                var selectedText="";
                                if(editObjectValues.id===e.id){
                                    selectedText="selected"
                                }
                                $("#"+obj.columnName).append("<option "+selectedText+" value=\""+e.id+"\">"+valueOfThis+"</option>");
                           });
                        });
                    }
                });
                var inputElementBack="<input type='button' class=\"btn btn-secondary form-control\"  id='"+$("#tableSelect").val()+"_FormCancel' value='Cancel & Back' name='"+$("#tableSelect").val()+"_FormCancel'/>"
                var inputElement="<input type='button' class=\"btn btn-primary form-control\"  id='"+$("#tableSelect").val()+"_FormSubmit' value='Submit Form' name='"+$("#tableSelect").val()+"_FormSubmit'/>"
                $("#createForm").append("<div class=\"row mb-3\"><div class=\"col col-3\">"+inputElementBack+"</div><div class=\"col col-3\">"+inputElement+"</div></div>");
                //Adding ID.. This can be dynamic from Backend may be #TODO
                var inputElementHidden="<input type='hidden' class=''  id='id_FormSubmit' value='"+id+"' name='id'/>"
                $("#createForm").append("<div class=\"row mb-3\"><div class=\"col col-3\"></div><div class=\"col col-3\">"+inputElementHidden+"</div></div>");
                $('#'+$("#tableSelect").val()+'_FormCancel').on('click', function() {
                    $('#tableSelect').trigger('change');
                });
                $('#'+$("#tableSelect").val()+'_FormSubmit').on('click', function() {
                                                    // Serialize form data to JSON
                    var formData = {};
                    $.each($('#createForm').serializeArray(), function(i, field) {
                      formData[field.name] = field.value;
                    });
                    console.log(formData);

                    // Send the data using AJAX
                    $.ajax({
                      type: 'PUT',
                      url: $('#createForm').attr('action'), // Form action URL
                      data: JSON.stringify(formData),
                      contentType: 'application/json',
                      success: function(response) {
                        // Handle success response

                        console.log('Success:', response);
                        $('#tableSelect').trigger('change');
                      },
                      error: function(error) {
                        // Handle error response
                        console.error('Error:', error);
                      }
                    });
              });

             });

      };

      function createDeleteForm(id){
            console.log('Delete  for '+id);
            var rep = confirm("Are you sure want to delete this record !");
            console.log(rep);

            if(rep){
               $.ajax({
                  type: 'DELETE',
                  url: "http://localhost:8080/api/v1/table/delete/"+$("#conSelect").val()+"/"+$("#tableSelect").val()+"/"+id, // Form action URL
                  data: {},
                  contentType: 'application/json',
                  success: function(response) {
                    // Handle success response
                    console.log('Success:', response);
                    $('#tableSelect').trigger('change');
                  },
                  error: function(error) {
                    // Handle error response
                    console.error('Error:', error);
                  }
                });
            }
            else{

            }
      };



