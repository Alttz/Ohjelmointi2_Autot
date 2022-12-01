//funktio tietojen hakemista varten. Kutsutaan backin GET metodia
function haeAutot() {
	let url = "autot?hakusana=" + document.getElementById("hakusana").value; 
	let requestOptions = {
        method: "GET",
        headers: { "Content-Type": "application/x-www-form-urlencoded" }       
    };    
    fetch(url, requestOptions)
    .then(response => response.json())//Muutetaan vastausteksti JSON-objektiksi 
   	.then(response => printItems(response)) 
   	.catch(errorText => console.error("Fetch failed: " + errorText));
}

//Kirjoitetaan tiedot taulukkoon JSON-objektilistasta
function printItems(respObjList){
	//console.log(respObjList);
	let htmlStr="";
	for(let item of respObjList){//yksi kokoelmalooppeista		
    	htmlStr+="<tr id='rivi_"+item.id+"'>";
    	htmlStr+="<td>"+item.rekno+"</td>";
    	htmlStr+="<td>"+item.merkki+"</td>";
    	htmlStr+="<td>"+item.malli+"</td>";
    	htmlStr+="<td>"+item.vuosi+"</td>";    
    	htmlStr+="<td><a href='muutaauto.jsp?id="+item.id+"'>Muuta</a>&nbsp;";
    	htmlStr+="<span class='poista' onclick=varmistaPoisto("+item.id+",'"+encodeURI(item.rekno)+"')>Poista</span></td>"; //encodeURI() muutetaan erikoismerkit, välilyönnit jne. UTF-8 merkeiksi.
    	htmlStr+="</tr>";    	
	}	
	document.getElementById("tbody").innerHTML = htmlStr;	
}

//funktio tietojen lisäämistä varten. Kutsutaan backin POST-metodia ja välitetään kutsun mukana auton tiedot json-stringinä.
function lisaaTiedot() {
	let formData = serialize_form(document.lomake); //Haetaan tiedot lomakkeelta ja muutetaan JSON-stringiksi
	// console.log(formData);
	let url = "autot";    
    let requestOptions = {
        method: "POST", //Lisätään auto
        headers: { "Content-Type": "application/json; charset=UTF-8" }, 
    	body: formData
    };    
    fetch(url, requestOptions)
    .then(response => response.json())//Muutetaan vastausteksti JSON-objektiksi
   	.then(responseObj => {	
   		//console.log(responseObj);
   		if(responseObj.response==0){
   			document.getElementById("ilmo").innerHTML = "Auton lisäys epäonnistui.";	
        }else if(responseObj.response==1){ 
        	document.getElementById("ilmo").innerHTML = "Auton lisäys onnistui.";
			document.lomake.reset(); //Tyhjennetään auton lisäämisen lomake		        	
		}
		setTimeout(function(){ document.getElementById("ilmo").innerHTML=""; }, 3000);
   	})
   	.catch(errorText => console.error("Fetch failed: " + errorText));
}

//funktio tietojen päivittämistä varten. Kutsutaan backin PUT-metodia ja välitetään kutsun mukana uudet tiedot json-stringinä.
function paivitaTiedot() {
	let formData = serialize_form(lomake); //Haetaan tiedot lomakkeelta ja muutetaan JSON-stringiksi
	// console.log(formData);
	let url = "autot";    
    let requestOptions = {
        method: "PUT", //Muutetaan auto
        headers: { "Content-Type": "application/json; charset=UTF-8" }, 
    	body: formData
    };    
    fetch(url, requestOptions)
    .then(response => response.json())//Muutetaan vastausteksti JSON-objektiksi
   	.then(responseObj => {	
   		//console.log(responseObj);
   		if(responseObj.response==0){
   			document.getElementById("ilmo").innerHTML = "Auton muutos epäonnistui.";	
        } else if(responseObj.response==1){ 
        	document.getElementById("ilmo").innerHTML = "Auton muutos onnistui.";
			document.lomake.reset(); //Tyhjennetään auton muutos lomake		        	
		}
   	})
   	.catch(errorText => console.error("Fetch failed: " + errorText));
}

//Poistetaan auto kutsumalla backin DELETE-metodia ja välittämällä sille poistettavan auton id
function poistaAuto(id, rekno) {
	let url = "autot?id=" + id;
	let requestOptions = {
		method: "DELETE"
	};
	fetch(url, requestOptions)
	.then(response => response.json()) //Muutetaan vastausteksti JSON-objektiksi
	.then(responseObj => {
		// console.log(responseObj);
		if(responseObj.response==0) {
			alert("Auton poisto epäonnistui.");
		} else if(responseObj.response==1) {
			document.getElementById("rivi_"+id).style.backgroundColor="red";
			alert("Auton " +decodeURI(rekno) +" poisto onnistui."); //decodeURI() muutetaan enkoodatut merkit takaisin normaaliksi kirjoitukseksi
			haeAutot();
		}
	})
	.catch(errorText => console.error("Fetch failed: " + errorText));
}

//Haetaan muutettavan auton tiedot. Kutsutaan backin GET-metodia ja välitetään kutsun mukana muutettavan tiedon id
function haeAuto() {
	let url = "autot?id=" + requestURLParam("id"); //requestURLParam() on funktio, jolla voidaan hakea urlista arvo avaimen perusteella. Löytyy main.js -tiedostosta 	
	//console.log(url);
	let requestOptions = {
        method: "GET",
        headers: { "Content-Type": "application/x-www-form-urlencoded" }       
    };    
    fetch(url, requestOptions)
    .then(response => response.json())//Muutetaan vastausteksti JSON-objektiksi 
   	.then(response => {
	   	//console.log(response);
		document.getElementById("id").value=response.id;
		document.getElementById("rekno").value=response.rekno;
		document.getElementById("merkki").value=response.merkki;
		document.getElementById("malli").value=response.malli;
		document.getElementById("vuosi").value=response.vuosi;
	})
   	.catch(errorText => console.error("Fetch failed: " + errorText));
}