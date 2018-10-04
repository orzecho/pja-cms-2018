# Specyfikacja funkcjonalna "Task tracker"

## Wymagania
### Zadania
* dodawanie zadań
* przypisywanie zadań do projektów
* przypisywanie zadań do milestonów
* przypisywanie użytkowników do zadań (asignee)
* przypisywanie obserwatorów do zadania
### Uprawnienia
* użytkownik może tworzyć nowe zadania
* użytkownik może edytować i usuwać własne zadania
* administrator może edytować i usuwać wszystkie zadania
* menedżer projektów może tworzyć, edytować i usuwać projekty
* menedżer zadań może tworzyć, edytować i usuwać milestony
* menedżer może przypisyawć użykowników do projektów
* użytkownik może widzieć wszystkie zadania z przypisanego projektu
* administrator ma wszystkie uprawnienia menedżera projektów
### Integracje
* raz na tydzień system wysyła podsumowanie zadań użytkownika
* za każdym razem gdy zmienia się status lub przypisany użytkownik zadania wysyła informację na kanał discorda lub slacka (webhook)
* do projektu można przypisać repozytorium githuba, system wyświetla ostatni commit
