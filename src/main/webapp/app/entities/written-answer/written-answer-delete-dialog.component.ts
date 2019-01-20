import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWrittenAnswer } from 'app/shared/model/written-answer.model';
import { WrittenAnswerService } from './written-answer.service';

@Component({
    selector: 'jhi-written-answer-delete-dialog',
    templateUrl: './written-answer-delete-dialog.component.html'
})
export class WrittenAnswerDeleteDialogComponent {
    writtenAnswer: IWrittenAnswer;

    constructor(
        private writtenAnswerService: WrittenAnswerService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.writtenAnswerService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'writtenAnswerListModification',
                content: 'Deleted an writtenAnswer'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-written-answer-delete-popup',
    template: ''
})
export class WrittenAnswerDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ writtenAnswer }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(WrittenAnswerDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.writtenAnswer = writtenAnswer;
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
