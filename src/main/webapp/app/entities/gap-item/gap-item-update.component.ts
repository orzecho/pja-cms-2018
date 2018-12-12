import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IGapItem } from 'app/shared/model/gap-item.model';
import { GapItemService } from './gap-item.service';
import { IFillingGapsTestItem } from 'app/shared/model/filling-gaps-test-item.model';
import { FillingGapsTestItemService } from 'app/entities/filling-gaps-test-item';

@Component({
    selector: 'jhi-gap-item-update',
    templateUrl: './gap-item-update.component.html'
})
export class GapItemUpdateComponent implements OnInit {
    private _gapItem: IGapItem;
    isSaving: boolean;

    fillinggapstestitems: IFillingGapsTestItem[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private gapItemService: GapItemService,
        private fillingGapsTestItemService: FillingGapsTestItemService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ gapItem }) => {
            this.gapItem = gapItem;
        });
        this.fillingGapsTestItemService.query().subscribe(
            (res: HttpResponse<IFillingGapsTestItem[]>) => {
                this.fillinggapstestitems = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.gapItem.id !== undefined) {
            this.subscribeToSaveResponse(this.gapItemService.update(this.gapItem));
        } else {
            this.subscribeToSaveResponse(this.gapItemService.create(this.gapItem));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IGapItem>>) {
        result.subscribe((res: HttpResponse<IGapItem>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackFillingGapsTestItemById(index: number, item: IFillingGapsTestItem) {
        return item.id;
    }
    get gapItem() {
        return this._gapItem;
    }

    set gapItem(gapItem: IGapItem) {
        this._gapItem = gapItem;
    }
}
