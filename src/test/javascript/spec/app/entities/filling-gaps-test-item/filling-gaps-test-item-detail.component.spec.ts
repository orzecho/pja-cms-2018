/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NyanTestModule } from '../../../test.module';
import { FillingGapsTestItemDetailComponent } from 'app/entities/filling-gaps-test-item/filling-gaps-test-item-detail.component';
import { FillingGapsTestItem } from 'app/shared/model/filling-gaps-test-item.model';

describe('Component Tests', () => {
    describe('FillingGapsTestItem Management Detail Component', () => {
        let comp: FillingGapsTestItemDetailComponent;
        let fixture: ComponentFixture<FillingGapsTestItemDetailComponent>;
        const route = ({ data: of({ fillingGapsTestItem: new FillingGapsTestItem(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [FillingGapsTestItemDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(FillingGapsTestItemDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FillingGapsTestItemDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.fillingGapsTestItem).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
