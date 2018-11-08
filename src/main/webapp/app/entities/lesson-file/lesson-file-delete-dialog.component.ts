import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILessonFile } from 'app/shared/model/lesson-file.model';
import { LessonFileService } from './lesson-file.service';

@Component({
    selector: 'jhi-lesson-file-delete-dialog',
    templateUrl: './lesson-file-delete-dialog.component.html'
})
export class LessonFileDeleteDialogComponent {
    lessonFile: ILessonFile;

    constructor(private lessonFileService: LessonFileService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.lessonFileService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'lessonFileListModification',
                content: 'Deleted an lessonFile'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-lesson-file-delete-popup',
    template: ''
})
export class LessonFileDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ lessonFile }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(LessonFileDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.lessonFile = lessonFile;
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
