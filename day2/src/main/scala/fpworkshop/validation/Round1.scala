package fpworkshop

object Round1 {

  // Model central behaviour and data (function and ADT)

  /*

    Tutto parte con l'analisi e semplificazione del problema al fine di trovare l'astrazione base.
    Per esempio vorrei poter valdiare una intera "request" per prenotare una camera di hotel,
    la quale sarà composta da tanti field, ogniuno andrà a sua volta validato singolarmente.
    Per alcuni field sarà applicata una sola regola, per altri più regole, in alcuni casi anche dipendenti fra loro.
    Esempi:
    - stringa non vuota
    - intero maggiore di zero
    - stringa che rappresenta un numero maggiore di zero (due regole dipendenti)

    tutte queste regole sono dei behaviour quindi vogliamo modellarli con una funzione.

    type Rule = ??? => ???

    Una funzione è qualcosa che prende un input e produce un output. Quali in questo caso?
    Soffermiamoci sull'input, quale può essere il tipo giusto? String? Int? Request?
    Se proviamo a pensare alle possibili funzioni "checkGtZero" oppure "checkNotEmpty" ci accorgiamo
    che i tipi di ingresso cambiano quindi conviene utilizzare un tipo generico A.
    Ma più in generale è difficile conoscere a monte il tipo preciso e spesso nemmeno esiste quindi
    partite sempre con il tipo generico, un simbolo questo vi permetterà di creare codice più flessibile
    in ambienti fortemente tipizzati.

    type Rule = A => ???

    Mentre per l'output? Nel mondo OOP potremmo immagianare checkGtZero come una funzione che prende
    un valore lo controlla tramite un if e tira un eccezione nel caso non sia valido, qualcosa tipo:

    def checkGtZero(v: Int): Unit =
      if (v <= 0) throw new Exception("...")

    Una funzione di questo genere, per quanto modellabile, romperebbe la composizione per due motivi:
    - return Unit
    - throw Exception

    Senza un valore di ritorno non possiamo per esempio comporre due regole in sequenza
    per creare una regola composta come "stringa numerica maggiore di zero".
    Meglio censire un tipo di ritorno che non sia Unit e qui vale lo stesso discorso
    fatto per l'input, potreste fare un analisi approfondita ma il fatto è che
    l'uso di un tipo generico è la scelta vincente.

    type Rule[A, B] = A => B

    Rimane da modellare il throw Exception. L'intendo dietro a questa scelta è quella di indicare
    più di un possibile output ovvero Unit or Exception. Per non rompere la composability dobbiamo
    sfruttare un tipo (esistente o custom) che esprima questo intento. In questo caso parliamo di
    un data type e visto che abbiamo usato la parola 'or' ci interessa utilizzare un
    Algebraic Data Type in particolare un Sum Type. Ne esistono tanti built-in personalmente
    provo a partire da questi se poi risultano essere una coperta corta allora ne introduco uno custom.
    I disponibili per i casi di errore sono: Option, Either, Try. Quale prendere?
    Per scegliere dobbiamo pensare come vogliamo utilizzare questi effetti alla fine del programma.
    Vogliamo un messaggio di errore? Si, allora Option è scartato. Dobbiamo rimanere accoppiati al
    concetto di exceptio perchè sfruttiamo librerie java che vivono in quel mondo? No, allora Try è scartato.
    Rimane Either, ok ma come modelliamo il tipo di errore? Possiamo fare direttamente una stringa oppure usare
    un enumerato o sum type, ma in particolare ci interessa sapere se avremo a che fare con un solo errore o con N errori.

    type Rule[A, B] = A => Either[List[ValidationError], B]

    Fantastico. Ultimissimo passo per quanto con i type alias si riesca a fare grandi cose,
    rimangono pur sempre un tool con poteri limitati, pensato per cambiare il nome alle cose.
    La scelta idiomatica in Scala è l'uso di un trait.

    trait Rule[A, B] {
      def check(value: A): Either[List[ValidationError], B]
    }

 */

}
