import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWordsTest } from 'app/shared/model/words-test.model';
import { WordsTestService } from './words-test.service';

@Component({
    selector: 'jhi-words-test-delete-dialog',
    templateUrl: './words-test-delete-dialog.component.html'
})
export class WordsTestDeleteDialogComponent {
    wordsTest: IWordsTest;

    constructor(private wordsTestService: WordsTestService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.wordsTestService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'wordsTestListModification',
                content: 'Deleted an wordsTest'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-words-test-delete-popup',
    template: ''
})
export class WordsTestDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ wordsTest }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(WordsTestDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.wordsTest = wordsTest;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
