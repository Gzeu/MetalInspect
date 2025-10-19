# Arhitectură & Design

## Layere (Clean Architecture)
- Data: Room, DAO, Repositories, PDF, Export/Backup
- Domain: UseCases, Models, Validators
- Presentation: MVVM, ViewModels (StateFlow), Fragments, Adapters

## Flux de date
```
UI (Fragment) ↔ ViewModel (StateFlow)
      ↓                 ↑
   UseCase ↔ Repository ↔ Room (DAO)
```

## Decizii cheie
- 100% offline; fără dependențe de rețea
- CameraX pentru stabilitate pe device-uri entry-level
- iText7 pentru PDF (rapoarte corporate-ready)
- SQLCipher opțional pentru criptarea DB
- Hilt DI pentru testabilitate și modularitate

## Navigație
- Navigation Component + Safe Args
- nav_graph.xml cu rute pentru listă/creare/detalii/cameră/galerie/checklist/settings

## Performanță
- Limitare dimensiune imagini, compresie JPEG, lazy loading
- Indexare DB, query-uri optimizate
- ProGuard/R8 pentru release
