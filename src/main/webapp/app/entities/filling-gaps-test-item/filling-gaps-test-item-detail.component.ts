import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFillingGapsTestItem } from 'app/shared/model/filling-gaps-test-item.model';

@Component({
    selector: 'jhi-filling-gaps-test-item-detail',
    templateUrl: './filling-gaps-test-item-detail.component.html'
})
export class FillingGapsTestItemDetailComponent implements OnInit {
    fillingGapsTestItem: IFillingGapsTestItem;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ fillingGapsTestItem }) => {
            this.fillingGapsTestItem = fillingGapsTestItem;
        });
    }

    previousState() {
        window.history.back();
    }
}
