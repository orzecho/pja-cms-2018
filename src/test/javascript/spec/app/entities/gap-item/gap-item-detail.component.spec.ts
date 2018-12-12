/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NyanTestModule } from '../../../test.module';
import { GapItemDetailComponent } from 'app/entities/gap-item/gap-item-detail.component';
import { GapItem } from 'app/shared/model/gap-item.model';

describe('Component Tests', () => {
    describe('GapItem Management Detail Component', () => {
        let comp: GapItemDetailComponent;
        let fixture: ComponentFixture<GapItemDetailComponent>;
        const route = ({ data: of({ gapItem: new GapItem(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [GapItemDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(GapItemDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(GapItemDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.gapItem).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
