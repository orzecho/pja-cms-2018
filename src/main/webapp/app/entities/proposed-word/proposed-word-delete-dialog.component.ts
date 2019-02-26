import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProposedWord } from 'app/shared/model/proposed-word.model';
import { ProposedWordService } from './proposed-word.service';

@Component({
    selector: 'jhi-proposed-word-delete-dialog',
    templateUrl: './proposed-word-delete-dialog.component.html'
})
export class ProposedWordDeleteDialogComponent {
    proposedWord: IProposedWord;

    constructor(
        private proposedWordService: ProposedWordService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.proposedWordService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'proposedWordListModification',
                content: 'Deleted an proposedWord'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-proposed-word-delete-popup',
    template: ''
})
export class ProposedWordDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ proposedWord }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ProposedWordDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.proposedWord = proposedWord;
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
