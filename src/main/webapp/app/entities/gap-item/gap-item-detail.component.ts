import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGapItem } from 'app/shared/model/gap-item.model';

@Component({
    selector: 'jhi-gap-item-detail',
    templateUrl: './gap-item-detail.component.html'
})
export class GapItemDetailComponent implements OnInit {
    gapItem: IGapItem;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ gapItem }) => {
            this.gapItem = gapItem;
        });
    }

    previousState() {
        window.history.back();
    }
}
