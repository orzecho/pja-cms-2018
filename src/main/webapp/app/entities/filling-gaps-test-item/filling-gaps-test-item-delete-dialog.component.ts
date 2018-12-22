import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFillingGapsTestItem } from 'app/shared/model/filling-gaps-test-item.model';
import { FillingGapsTestItemService } from './filling-gaps-test-item.service';

@Component({
    selector: 'jhi-filling-gaps-test-item-delete-dialog',
    templateUrl: './filling-gaps-test-item-delete-dialog.component.html'
})
export class FillingGapsTestItemDeleteDialogComponent {
    fillingGapsTestItem: IFillingGapsTestItem;

    constructor(
        private fillingGapsTestItemService: FillingGapsTestItemService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.fillingGapsTestItemService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'fillingGapsTestItemListModification',
                content: 'Deleted an fillingGapsTestItem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-filling-gaps-test-item-delete-popup',
    template: ''
})
export class FillingGapsTestItemDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ fillingGapsTestItem }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FillingGapsTestItemDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.fillingGapsTestItem = fillingGapsTestItem;
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
