
# Esempio di Pagamenti con Spring Boot - Dependency Injection e Factory Pattern

## Introduzione
In questo esempio, implementiamo un sistema di **pagamenti** con diverse modalità (`Carta di Credito`, `PayPal`, `Bonifico Bancario`) utilizzando **Spring Boot**. L'approccio si concentra sull'uso di **Dependency Injection (DI)** e del **Factory Pattern** per separare le responsabilità e rendere il codice facilmente estensibile e manutenibile. Inoltre, il progetto dimostra come il **polimorfismo** consenta di invocare il metodo `paga()` sui diversi tipi di pagamento, mantenendo il codice pulito e flessibile.

### Obiettivo
- **Gestire diversi metodi di pagamento** in un'applicazione Spring Boot.
- Utilizzare **Dependency Injection** per la gestione delle dipendenze.
- Applicare il **Factory Pattern** per creare oggetti di pagamento in modo dinamico, riducendo il legame tra le classi.
- Dimostrare l'utilizzo del **polimorfismo** per un comportamento dinamico delle modalità di pagamento.

---

## Componenti dell'applicazione

1. **Model - Classi astratte e concrete (Pagamento)**

   La classe `Pagamento` è una classe astratta che definisce il comportamento comune di tutti i metodi di pagamento. Ogni tipo di pagamento (`CartaDiCredito`, `PayPal`, `Bonifico Bancario`) è rappresentato da una classe che estende `Pagamento` e implementa il metodo `paga()`.

   ```java
   public abstract class Pagamento {
       protected double importo;

       public void setImporto(double importo) {
           this.importo = importo;
       }

       public abstract void paga(); // Polimorfismo: il comportamento cambia in base alla classe concreta
   }
   ```

   Le classi concrete implementano il metodo `paga()` per ciascun tipo di pagamento:

   ```java
   @Component("carta")
   public class CartaDiCredito extends Pagamento {
       @Override
       public void paga() {
           System.out.println("Pagamento di " + importo + "€ effettuato con Carta di Credito.");
       }
   }
   ```

   ```java
   @Component("paypal")
   public class PayPal extends Pagamento {
       @Override
       public void paga() {
           System.out.println("Pagamento di " + importo + "€ effettuato con PayPal.");
       }
   }
   ```

   ```java
   @Component("bonifico")
   public class BonificoBancario extends Pagamento {
       @Override
       public void paga() {
           System.out.println("Pagamento di " + importo + "€ effettuato con Bonifico Bancario.");
       }
   }
   ```

2. **Service - Factory per la creazione dei pagamenti**

   La classe `PagamentoFactory` implementa il **Factory Pattern**: riceve il tipo di pagamento come stringa e restituisce l'istanza corrispondente, utilizzando **Dependency Injection** per iniettare la mappa dei pagamenti.

   ```java
   @Service
   public class PagamentoFactory {
       private final Map<String, Pagamento> metodiPagamento;

       @Autowired
       public PagamentoFactory(Map<String, Pagamento> metodiPagamento) {
           this.metodiPagamento = metodiPagamento;
       }

       public Pagamento getPagamento(String tipo, double importo) {
           Pagamento pagamento = metodiPagamento.get(tipo.toLowerCase());
           if (pagamento == null) {
               throw new IllegalArgumentException("Metodo di pagamento non supportato: " + tipo);
           }
           pagamento.setImporto(importo);
           return pagamento;
       }
   }
   ```

   Spring inietta automaticamente la mappa `metodiPagamento` che contiene le diverse implementazioni dei pagamenti, come `CartaDiCredito`, `PayPal`, `BonificoBancario`.

3. **Controller - Espone un'API REST per eseguire i pagamenti**

   Il controller `PagamentoController` è responsabile di ricevere le richieste HTTP e di invocare la logica di pagamento tramite la `PagamentoFactory`. Le dipendenze vengono iniettate tramite il costruttore grazie alla DI di Spring.

   ```java
   @RestController
   @RequestMapping("/pagamenti")
   public class PagamentoController {
       private final PagamentoFactory pagamentoFactory;

       @Autowired
       public PagamentoController(PagamentoFactory pagamentoFactory) {
           this.pagamentoFactory = pagamentoFactory;
       }

       @PostMapping("/{tipo}/{importo}")
       public String effettuaPagamento(@PathVariable String tipo, @PathVariable double importo) {
           Pagamento pagamento = pagamentoFactory.getPagamento(tipo, importo);
           pagamento.paga(); // Polimorfismo: il metodo paga() cambia comportamento a seconda dell'oggetto
           return "Pagamento di " + importo + "€ elaborato con successo via " + tipo;
       }
   }
   ```

---

## Come funziona il codice?

1. **Iniezione delle dipendenze**:  
   Grazie alla **Dependency Injection** di Spring, i componenti (`Pagamento`, `PagamentoFactory`, `PagamentoController`) sono **gestiti da Spring**. Questo significa che non dobbiamo preoccuparci di creare istanze manualmente con `new`. Spring crea e gestisce la vita degli oggetti.

2. **Factory Pattern**:  
   La `PagamentoFactory` è responsabile della **creazione dei pagamenti** a seconda del tipo passato dal client. In pratica, la factory agisce come una "fabbrica" di oggetti, restituendo l'istanza corretta del pagamento (ad esempio, `CartaDiCredito`, `PayPal` o `BonificoBancario`).

3. **Polimorfismo**:  
   Il polimorfismo entra in gioco quando chiamiamo il metodo `paga()` su un oggetto di tipo `Pagamento`. Sebbene tutte le istanze siano di tipo `Pagamento`, il comportamento del metodo `paga()` cambia in base alla classe concreta (ad esempio, `CartaDiCredito`, `PayPal`, o `BonificoBancario`). Questo consente di eseguire il pagamento in modo dinamico, senza bisogno di conoscere il tipo specifico di pagamento.

4. **Controllo tramite REST API**:  
   Il controller espone un'API REST per **eseguire i pagamenti**. Ogni volta che viene ricevuta una richiesta, la `PagamentoFactory` crea l'istanza di pagamento appropriata e invoca il metodo `paga()`.

---

## Vantaggi dell'Approccio
- **Modularità**: La logica di pagamento è ben separata tra le classi `Pagamento` e le sue implementazioni concrete.
- **Scalabilità**: Se dobbiamo aggiungere nuovi metodi di pagamento, basta aggiungere una nuova classe che estende `Pagamento` e registrarla come bean con `@Component`.
- **Polimorfismo**: Il comportamento del metodo `paga()` cambia dinamicamente a seconda del tipo di pagamento, mantenendo il codice semplice e flessibile.
- **Testabilità**: Grazie alla **Dependency Injection**, possiamo facilmente **mockare le dipendenze** in fase di testing, migliorando la testabilità del sistema.
- **Manutenibilità**: Il codice è facile da mantenere, poiché le dipendenze sono gestite centralmente da Spring e possiamo estendere il sistema con nuove funzionalità senza modificare il codice esistente.

---

## Conclusioni
In questo esempio, abbiamo utilizzato:
- **Spring Boot** per sfruttare le potenzialità di **Dependency Injection**.
- Il **Factory Pattern** per separare la creazione degli oggetti dal codice di business.
- **Rest API** per interagire con il sistema di pagamenti in modo dinamico e semplice.
- **Polimorfismo** per adattare il comportamento del sistema in base al tipo di pagamento selezionato.

Questo approccio permette di **aggiungere facilmente nuovi metodi di pagamento** e rende l'applicazione più **flessibile**, **modulare** e **testabile**. 🚀

---

## Implementazioni e miglioramenti futuri
- Aggiungere **autenticazione** per proteggere le API.
- Gestire **errori e eccezioni** in modo più robusto (ad esempio, se il metodo di pagamento non è supportato).
- Integrare un **sistema di logging** per tracciare le transazioni.
- **Scrivere test unitari** per la `PagamentoFactory` e il `PagamentoController`.

---

### Esempio di test

1. **Clonare il Repository**:
   ```bash
   git clone git@github.com:JavaSoftwareEntwickler/factory-pattern-and-DI.git
   ```
2. **Eseguire l'applicazione**:
   ```bash
   cd factory-pattern-and-DI
   mvn clean install
   mvn spring-boot:run
   ```
3. **Testare l'API**:
   ```bash
   curl -X POST http://localhost:8080/pagamenti/carta/100
   ```
