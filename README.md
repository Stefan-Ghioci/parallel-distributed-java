
# **PPD: Laborator 4 - Server/Client**

## **Analiza cerințelor**

### **Cerințe**
O sala de concerte vinde bilete la spectacolele organizate printr-o aplicatie client-server. Sala are trei categorii de locuri cu preturi diferite. 

### **Constrângeri**
* Periodic sistemul face o verificare a locurilor vandute prin verificarea corespondentei corecte intre locurile libere si vanzarile facute (de la ultima verificare pana in prezent), sumele incasate in aceeasi perioada si soldul total. 
* Sistemul foloseste un mecanism de tip ‘Thread-Pool’ pentru rezolvarea concurenta a vanzarilor. Pentru a testare se va folosi un thread care initiaza/creeaza la interval de 5 sec o noua cerere de vanzare bilete folosind date generate aleatoriu. 
* Pentru verificare se cere salvarea pe suport extern a soldului, a listei vanzarilor si a rezultatelor operatiilor de verificare executate periodic. 

## **Proiectare**

### **Sumar**

#### Server
* Limbajul folosit este **Java**, folosind framework-ul **Spring**.
* Structura modulului prezintă:
 1. clase pentru fiecare entitate
    1. Balanța
    2. Prețuri (in funcție de tipul de loc)
    3. Locuri
    4. Vânzări
    5. Bilete
    6. Spectacole
 2. **repository** **generic** de tip **CRUD** pentru fiecare entitate
 3. **service** de tip **async** ce rezolvă operații pe repo sub formă de **tranzacții**
 4. **controller** de tip **REST** pentru request-urile client-ului
    1. verificarea balanței (banii incasați vs. suma banilor de pe fiecare bilet)
    2. lista cu bilete disponibile pentru fiecare spectacol si prețurile aferente
    3. lista cu spectacole
    4. vânzare bilete

#### Client
* Limbajul folosit este **React** 
* Utilizatorul poate selecta un spectacol si cumpara biletele in functie de categorie
* Un client de test execută care executa la interval de 5 secunde o vanzare cu date la întâmplare și verifică balanța

### **Descrierea algoritmului**

La pornirea serverului, un client se poate conecta folosind comanda **fetch**, cu metode de tip **GET** sau **POST** (pentru request-ul de la vânzare).

Serverul apeleaza service-ul pentru a satisface cererea clientului. Service-ul apelează metode de tip **synchronized** (pentru a îndeplini condiția de **tranzacție**), ce interoghează baza de date si prelucrează datele în funcție de cererea de la client. 

Metodele din service sunt gestionate de un **executor** de tip **asynchronous**, ce pornește cu un număr de **3** thread-uri, poate ajunge până la **10** și are o coadă de maximm **100** de cereri. 

Astfel, se pot conecta clienți ce fac un număr de maximum 100 cereri simultane la server, ce se rulează pe maximum **10** thread-uri simultane ce execută **tranzacții** **serializate**.
