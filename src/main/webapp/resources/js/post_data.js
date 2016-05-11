$.fn.serializeObject = function()    
{    
   var o = {};    
   var a = this.serializeArray();    
   $.each(a, function() {    
       if (o[this.name]) {    
           if (!o[this.name].push) {    
               o[this.name] = [o[this.name]];    
           }    
           o[this.name].push(this.value || '');    
       } else {    
           o[this.name] = this.value || '';    
       }    
   });
   return o;    
};

function postSubmit(url) {  
    var ExportForm = document.createElement("form");   
    ExportForm.method = "POST";
    ExportForm.action = url;
    var newElement = document.createElement("input");
    newElement.name = "data";
    var jsonuserinfo = $('#form').serializeObject();  
    newElement.value = JSON.stringify(jsonuserinfo);

    //alert('JSON:' + newElement.value);

    ExportForm.appendChild(newElement);
    document.body.appendChild(ExportForm);
    
    ExportForm.submit();  
};

