import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IFillingGapsTestItem } from 'app/shared/model/filling-gaps-test-item.model';
import { FillingGapsTestItemService } from './filling-gaps-test-item.service';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';

@Component({
    selector: 'jhi-filling-gaps-test-item-update',
    templateUrl: './filling-gaps-test-item-update.component.html'
})
export class FillingGapsTestItemUpdateComponent implements OnInit {
    private _fillingGapsTestItem: IFillingGapsTestItem;
    isSaving: boolean;

    tags: ITag[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private fillingGapsTestItemService: FillingGapsTestItemService,
        private tagService: TagService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ fillingGapsTestItem }) => {
            this.fillingGapsTestItem = fillingGapsTestItem;
        });
        this.tagService.query().subscribe(
            (res: HttpResponse<ITag[]>) => {
                this.tags = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.fillingGapsTestItem.id !== undefined) {
            this.subscribeToSaveResponse(this.fillingGapsTestItemService.update(this.fillingGapsTestItem));
        } else {
            this.subscribeToSaveResponse(this.fillingGapsTestItemService.create(this.fillingGapsTestItem));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IFillingGapsTestItem>>) {
        result.subscribe((res: HttpResponse<IFillingGapsTestItem>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackTagById(index: number, item: ITag) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get fillingGapsTestItem() {
        return this._fillingGapsTestItem;
    }

    set fillingGapsTestItem(fillingGapsTestItem: IFillingGapsTestItem) {
        this._fillingGapsTestItem = fillingGapsTestItem;
    }
}
