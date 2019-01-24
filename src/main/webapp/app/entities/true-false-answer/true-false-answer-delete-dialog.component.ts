import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITrueFalseAnswer } from 'app/shared/model/true-false-answer.model';
import { TrueFalseAnswerService } from './true-false-answer.service';

@Component({
    selector: 'jhi-true-false-answer-delete-dialog',
    templateUrl: './true-false-answer-delete-dialog.component.html'
})
export class TrueFalseAnswerDeleteDialogComponent {
    trueFalseAnswer: ITrueFalseAnswer;

    constructor(
        private trueFalseAnswerService: TrueFalseAnswerService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.trueFalseAnswerService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'trueFalseAnswerListModification',
                content: 'Deleted an trueFalseAnswer'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-true-false-answer-delete-popup',
    template: ''
})
export class TrueFalseAnswerDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ trueFalseAnswer }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TrueFalseAnswerDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.trueFalseAnswer = trueFalseAnswer;
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
