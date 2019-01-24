import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { Principal } from 'app/core';
import { TagService } from 'app/entities/tag';
import { ITag } from 'app/shared/model/tag.model';

@Component({
    selector: 'jhi-tag',
    templateUrl: './test.component.html'
})
export class TestComponent implements OnInit {
    currentAccount: any;
    tags: ITag[];
    availableTags: ITag[];
    error: any;
    success: any;
    testType = 'WRITTEN_MIXED';

    constructor(
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private tagService: TagService
    ) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
    }

    routeToTestPage() {
        const stringTags = this.tags.map(t => t.name).join(',');
        if (this.testType === 'GAPS') {
            this.router.navigate([`/test/${this.testType}/${stringTags}`]);
        } else {
            this.router.navigate([`/test/vocabulary/${this.testType}/${stringTags}`]);
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    search(event) {
        const req = {
            'name.contains': event.query
        };
        this.tagService.query(req).subscribe(response => {
            this.availableTags = response.body;
            console.log(this.availableTags);
        });
    }
}
