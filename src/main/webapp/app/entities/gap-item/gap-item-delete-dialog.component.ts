import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGapItem } from 'app/shared/model/gap-item.model';
import { GapItemService } from './gap-item.service';

@Component({
    selector: 'jhi-gap-item-delete-dialog',
    templateUrl: './gap-item-delete-dialog.component.html'
})
export class GapItemDeleteDialogComponent {
    gapItem: IGapItem;

    constructor(private gapItemService: GapItemService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.gapItemService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'gapItemListModification',
                content: 'Deleted an gapItem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-gap-item-delete-popup',
    template: ''
})
export class GapItemDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ gapItem }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(GapItemDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.gapItem = gapItem;
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
